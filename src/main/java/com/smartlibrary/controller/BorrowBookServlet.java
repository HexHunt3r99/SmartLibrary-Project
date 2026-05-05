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

public class BorrowBookServlet extends HttpServlet {
    
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
        
        int bookId = Integer.parseInt(request.getParameter("book_id"));
        String daysStr = request.getParameter("days");
        int days = RoleUtils.getBorrowDays(role);
        if (daysStr != null && !daysStr.isEmpty()) {
            try {
                days = Integer.parseInt(daysStr);
                if (days <= 0) days = RoleUtils.getBorrowDays(role);
            } catch (NumberFormatException e) {
                days = RoleUtils.getBorrowDays(role);
            }
        }
        
        try {
            Connection conn = DBConnection.getConnection();
            
            String checkAlreadySql = "SELECT borrow_id FROM borrows WHERE user_id = ? AND book_id = ? AND status = 'borrowed'";
            PreparedStatement checkAlreadyStmt = conn.prepareStatement(checkAlreadySql);
            checkAlreadyStmt.setInt(1, userId);
            checkAlreadyStmt.setInt(2, bookId);
            ResultSet alreadyRs = checkAlreadyStmt.executeQuery();
            
            if (alreadyRs.next()) {
                alreadyRs.close();
                checkAlreadyStmt.close();
                conn.close();
                out.print("{\"success\":false,\"message\":\"You already have this book borrowed\"}");
                return;
            }
            
            String checkSql = "SELECT available FROM books WHERE book_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                int available = rs.getInt("available");
                
                if (available > 0) {
                    String insertSql = "INSERT INTO borrows (user_id, book_id, due_date, status) VALUES (?, ?, CURRENT_TIMESTAMP + INTERVAL '1 day' * ?, 'borrowed')";
                    PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, bookId);
                    insertStmt.setInt(3, days);
                    insertStmt.executeUpdate();
                    
                    String updateSql = "UPDATE books SET available = available - 1 WHERE book_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setInt(1, bookId);
                    updateStmt.executeUpdate();
                    
                    insertStmt.close();
                    updateStmt.close();
                    
                    out.print("{\"success\":true,\"message\":\"Book borrowed successfully!\"}");
                } else {
                    out.print("{\"success\":false,\"message\":\"Book not available\"}");
                }
            } else {
                out.print("{\"success\":false,\"message\":\"Book not found\"}");
            }
            
            rs.close();
            checkStmt.close();
            conn.close();
            
        } catch (Exception e) {
            out.print("{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("user/books.jsp");
    }
}

