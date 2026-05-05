package com.smartlibrary.controller;

import com.smartlibrary.util.DBConnection;
import com.smartlibrary.util.RoleUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ReserveBookServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        String role = (String) session.getAttribute("role");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        if (userId == null) {
            out.print("{\"success\":false,\"message\":\"Please login first\"}");
            return;
        }
        
        String action = request.getParameter("action");
        
        if (action == null) {
            out.print("{\"success\":false,\"message\":\"Invalid action\"}");
            return;
        }
        
        try {
            Connection conn = DBConnection.getConnection();
            
            switch (action) {
                case "reserve":
                    handleReserve(request, response, conn, userId, out);
                    break;
                case "borrow":
                    handleBorrowFromReservation(request, response, conn, userId, role, out);
                    break;
                case "cancel":
                    handleCancelReservation(request, response, conn, userId, out);
                    break;
                default:
                    out.print("{\"success\":false,\"message\":\"Unknown action\"}");
            }
            
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}");
        }
    }
    
    private void handleReserve(HttpServletRequest request, HttpServletResponse response, 
            Connection conn, int userId, PrintWriter out) throws Exception {
        
        int bookId = Integer.parseInt(request.getParameter("book_id"));
        
        String checkExistingSql = "SELECT reservation_id, status, queue_position FROM reservations WHERE user_id = ? AND book_id = ? AND status IN ('active', 'pending', 'ready')";
        PreparedStatement checkExistingStmt = conn.prepareStatement(checkExistingSql);
        checkExistingStmt.setInt(1, userId);
        checkExistingStmt.setInt(2, bookId);
        ResultSet existingRs = checkExistingStmt.executeQuery();
        
        if (existingRs.next()) {
            String existingStatus = existingRs.getString("status");
            int queuePos = existingRs.getInt("queue_position");
            existingRs.close();
            checkExistingStmt.close();
            
            if (existingStatus.equals("ready")) {
                out.print("{\"success\":false,\"message\":\"This book is ready for pickup! Check your reservations.\"}");
            } else if (existingStatus.equals("active")) {
                out.print("{\"success\":false,\"message\":\"You already have an active reservation for this book.\"}");
            } else {
                out.print("{\"success\":false,\"message\":\"This book is already in queue at position #" + queuePos + "\"}");
            }
            return;
        }
        existingRs.close();
        checkExistingStmt.close();
        
        String checkSql = "SELECT available FROM books WHERE book_id = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setInt(1, bookId);
        ResultSet rs = checkStmt.executeQuery();
        
        if (!rs.next()) {
            rs.close();
            checkStmt.close();
            out.print("{\"success\":false,\"message\":\"Book not found\"}");
            return;
        }
        
        int available = rs.getInt("available");
        rs.close();
        checkStmt.close();
        
        if (available > 0) {
            String insertSql = "INSERT INTO reservations (user_id, book_id, reservation_date, expiration_date, status, queue_position) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '3 days', 'active', 1)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, userId);
            insertStmt.setInt(2, bookId);
            insertStmt.executeUpdate();
            insertStmt.close();
            
            out.print("{\"success\":true,\"message\":\"Book reserved successfully!\"}");
        } else {
            String queueSql = "SELECT COALESCE(MAX(queue_position), 0) + 1 as next_position FROM reservations WHERE book_id = ? AND status = 'pending'";
            PreparedStatement queueStmt = conn.prepareStatement(queueSql);
            queueStmt.setInt(1, bookId);
            ResultSet queueRs = queueStmt.executeQuery();
            int queuePos = 1;
            if (queueRs.next()) {
                queuePos = queueRs.getInt("next_position");
            }
            queueRs.close();
            queueStmt.close();
            
            String insertSql = "INSERT INTO reservations (user_id, book_id, reservation_date, expiration_date, status, queue_position) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '3 days', 'pending', ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, userId);
            insertStmt.setInt(2, bookId);
            insertStmt.setInt(3, queuePos);
            insertStmt.executeUpdate();
            insertStmt.close();
            
            out.print("{\"success\":true,\"message\":\"Added to reservation queue at position #" + queuePos + "\"}");
        }
    }
    
    private void handleBorrowFromReservation(HttpServletRequest request, HttpServletResponse response,
            Connection conn, int userId, String role, PrintWriter out) throws Exception {
        
        int reservationId = Integer.parseInt(request.getParameter("reservation_id"));
        
        String checkSql = "SELECT r.*, b.available FROM reservations r JOIN books b ON r.book_id = b.book_id " +
            "WHERE r.reservation_id = ? AND r.user_id = ? AND r.status = 'ready'";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setInt(1, reservationId);
        checkStmt.setInt(2, userId);
        ResultSet rs = checkStmt.executeQuery();
        
        if (!rs.next()) {
            rs.close();
            checkStmt.close();
            out.print("{\"success\":false,\"message\":\"Reservation not found or not ready for pickup\"}");
            return;
        }
        
        int bookId = rs.getInt("book_id");
        int available = rs.getInt("available");
        rs.close();
        checkStmt.close();
        
        if (available <= 0) {
            out.print("{\"success\":false,\"message\":\"Book no longer available\"}");
            return;
        }
        
        String insertBorrowSql = "INSERT INTO borrows (user_id, book_id, due_date, status) " +
            "VALUES (?, ?, CURRENT_TIMESTAMP + INTERVAL '1 day' * ?, 'borrowed')";
        PreparedStatement insertBorrowStmt = conn.prepareStatement(insertBorrowSql);
        insertBorrowStmt.setInt(1, userId);
        insertBorrowStmt.setInt(2, bookId);
        insertBorrowStmt.setInt(3, RoleUtils.getBorrowDays(role));
        insertBorrowStmt.executeUpdate();
        insertBorrowStmt.close();
        
        String updateBookSql = "UPDATE books SET available = available - 1 WHERE book_id = ?";
        PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql);
        updateBookStmt.setInt(1, bookId);
        updateBookStmt.executeUpdate();
        updateBookStmt.close();
        
        String updateResSql = "UPDATE reservations SET status = 'completed' WHERE reservation_id = ?";
        PreparedStatement updateResStmt = conn.prepareStatement(updateResSql);
        updateResStmt.setInt(1, reservationId);
        updateResStmt.executeUpdate();
        updateResStmt.close();
        
        out.print("{\"success\":true,\"message\":\"Book borrowed successfully!\"}");
    }
    
    private void handleCancelReservation(HttpServletRequest request, HttpServletResponse response,
            Connection conn, int userId, PrintWriter out) throws Exception {
        
        int reservationId = Integer.parseInt(request.getParameter("reservation_id"));
        
        String checkSql = "SELECT * FROM reservations WHERE reservation_id = ? AND user_id = ? AND status IN ('active', 'pending')";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setInt(1, reservationId);
        checkStmt.setInt(2, userId);
        ResultSet rs = checkStmt.executeQuery();
        
        if (!rs.next()) {
            rs.close();
            checkStmt.close();
            out.print("{\"success\":false,\"message\":\"Reservation not found or already processed\"}");
            return;
        }
        
        int bookId = rs.getInt("book_id");
        String status = rs.getString("status");
        rs.close();
        checkStmt.close();
        
        String updateSql = "UPDATE reservations SET status = 'cancelled' WHERE reservation_id = ?";
        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
        updateStmt.setInt(1, reservationId);
        updateStmt.executeUpdate();
        updateStmt.close();
        
        if (status.equals("pending")) {
            String promoteSql = "UPDATE reservations SET queue_position = queue_position - 1 " +
                "WHERE book_id = ? AND status = 'pending' AND queue_position > " +
                "(SELECT queue_position FROM reservations WHERE reservation_id = ?)";
            PreparedStatement promoteStmt = conn.prepareStatement(promoteSql);
            promoteStmt.setInt(1, bookId);
            promoteStmt.setInt(2, reservationId);
            promoteStmt.executeUpdate();
            promoteStmt.close();
        }
        
        out.print("{\"success\":true,\"message\":\"Reservation cancelled successfully\"}");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("user/reservations.jsp");
    }
}
