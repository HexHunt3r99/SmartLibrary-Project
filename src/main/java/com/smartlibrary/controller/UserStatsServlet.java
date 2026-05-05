package com.smartlibrary.controller;

import com.smartlibrary.util.DBConnection;
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

public class UserStatsServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        
        if (userId == null) {
            out.print("{\"success\":false,\"message\":\"Please login first\"}");
            return;
        }
        
        try {
            Connection conn = DBConnection.getConnection();
            
            int borrowedBooks = getCount(conn, "SELECT COUNT(*) FROM borrows WHERE user_id = ? AND status = 'borrowed'", userId);
            int activeReservations = getCount(conn, "SELECT COUNT(*) FROM reservations WHERE user_id = ? AND status = 'active'", userId);
            double totalFines = getSum(conn, "SELECT COALESCE(SUM(amount), 0) FROM fines WHERE user_id = ? AND status = 'unpaid'", userId);
            
            int totalBorrows = getCount(conn, "SELECT COUNT(*) FROM borrows WHERE user_id = ?", userId);
            int lateReturns = getCount(conn, "SELECT COUNT(*) FROM borrows WHERE user_id = ? AND status = 'overdue'", userId);
            double totalFinesPaid = getSum(conn, "SELECT COALESCE(SUM(amount), 0) FROM fines WHERE user_id = ? AND status = 'paid'", userId);
            
            int readyPickup = getCount(conn, "SELECT COUNT(*) FROM reservations WHERE user_id = ? AND status = 'ready'", userId);
            int inQueue = getCount(conn, "SELECT COUNT(*) FROM reservations WHERE user_id = ? AND status = 'pending'", userId);
            int overdue = getCount(conn, "SELECT COUNT(*) FROM borrows WHERE user_id = ? AND status = 'overdue'", userId);
            
            String recentBorrowsJson = getRecentBorrows(conn, userId);
            String borrowedBooksJson = getBorrowedBookIds(conn, userId);
            String recentActivityJson = getRecentActivity(conn, userId);
            String reservationsJson = getUserReservations(conn, userId);
            String finesJson = getUserFines(conn, userId);
            
            String username = "";
            String fullName = "";
            PreparedStatement userStmt = conn.prepareStatement("SELECT username, full_name FROM users WHERE user_id = ?");
            userStmt.setInt(1, userId);
            ResultSet userRs = userStmt.executeQuery();
            if (userRs.next()) {
                username = escapeJson(userRs.getString("username"));
                fullName = escapeJson(userRs.getString("full_name"));
                if (fullName == null || fullName.isEmpty()) {
                    fullName = username;
                }
            }
            userRs.close();
            userStmt.close();
            
            out.print("{");
            out.print("\"success\":true,");
            out.print("\"username\":\"" + username + "\",");
            out.print("\"full_name\":\"" + fullName + "\",");
            out.print("\"borrowed_books\":" + borrowedBooks + ",");
            out.print("\"active_reservations\":" + activeReservations + ",");
            out.print("\"total_fines\":" + totalFines + ",");
            out.print("\"total_borrows\":" + totalBorrows + ",");
            out.print("\"returned_on_time\":" + (totalBorrows - lateReturns) + ",");
            out.print("\"late_returns\":" + lateReturns + ",");
            out.print("\"total_fines_paid\":" + totalFinesPaid + ",");
            out.print("\"ready_pickup\":" + readyPickup + ",");
            out.print("\"in_queue\":" + inQueue + ",");
            out.print("\"pending_renewal\":0,");
            out.print("\"overdue\":" + overdue + ",");
            out.print("\"recent_borrows\":" + recentBorrowsJson + ",");
            out.print("\"borrowed_book_ids\":" + borrowedBooksJson + ",");
            out.print("\"recent_activity\":" + recentActivityJson + ",");
            out.print("\"reservations\":" + reservationsJson + ",");
            out.print("\"fines\":" + finesJson);
            out.print("}");
            
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}");
        }
    }
    
    private int getCount(Connection conn, String sql, int userId) throws Exception {
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        int count = rs.next() ? rs.getInt(1) : 0;
        rs.close();
        stmt.close();
        return count;
    }
    
    private double getSum(Connection conn, String sql, int userId) throws Exception {
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        double sum = rs.next() ? rs.getDouble(1) : 0;
        rs.close();
        stmt.close();
        return sum;
    }
    
    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t")
                    .replace("\f", "\\f")
                    .replace("\b", "\\b");
    }
    
    private String getRecentBorrows(Connection conn, int userId) throws Exception {
        StringBuilder json = new StringBuilder("[");
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT b.borrow_id, bk.title, bk.author, b.borrow_date, b.due_date, b.status " +
            "FROM borrows b JOIN books bk ON b.book_id = bk.book_id " +
            "WHERE b.user_id = ? ORDER BY b.borrow_date DESC LIMIT 5"
        );
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        
        boolean first = true;
        while (rs.next()) {
            if (!first) json.append(",");
            first = false;
            
            String title = rs.getString("title");
            String author = rs.getString("author");
            java.sql.Date borrowedDate = rs.getDate("borrow_date");
            java.sql.Date dueDate = rs.getDate("due_date");
            String status = rs.getString("status");
            
            json.append("{");
            json.append("\"id\":").append(rs.getInt("borrow_id")).append(",");
            json.append("\"title\":\"").append(escapeJson(title != null ? title : "")).append("\",");
            json.append("\"author\":\"").append(escapeJson(author != null ? author : "")).append("\",");
            json.append("\"borrowed_date\":\"").append(borrowedDate != null ? borrowedDate.toString() : "").append("\",");
            json.append("\"due_date\":\"").append(dueDate != null ? dueDate.toString() : "").append("\",");
            json.append("\"status\":\"").append(escapeJson(status != null ? status : "")).append("\"");
            json.append("}");
        }
        
        rs.close();
        stmt.close();
        json.append("]");
        return json.toString();
    }
    
    private String getRecentActivity(Connection conn, int userId) throws Exception {
        StringBuilder json = new StringBuilder("[");
        
        PreparedStatement stmt = conn.prepareStatement(
            "(SELECT 'borrow' as type, b.borrow_id as id, bk.title, b.borrow_date as date, b.status " +
            "FROM borrows b JOIN books bk ON b.book_id = bk.book_id WHERE b.user_id = ?) " +
            "UNION ALL " +
            "(SELECT 'reservation' as type, r.reservation_id as id, bk.title, r.reservation_date as date, r.status " +
            "FROM reservations r JOIN books bk ON r.book_id = bk.book_id WHERE r.user_id = ?) " +
            "UNION ALL " +
            "(SELECT 'fine' as type, f.fine_id as id, '' as title, CURRENT_DATE as date, f.status " +
            "FROM fines f WHERE f.user_id = ?) " +
            "ORDER BY date DESC LIMIT 10"
        );
        stmt.setInt(1, userId);
        stmt.setInt(2, userId);
        stmt.setInt(3, userId);
        ResultSet rs = stmt.executeQuery();
        
        boolean first = true;
        while (rs.next()) {
            if (!first) json.append(",");
            first = false;
            
            json.append("{");
            json.append("\"type\":\"").append(rs.getString("type")).append("\",");
            json.append("\"id\":").append(rs.getInt("id")).append(",");
            json.append("\"title\":\"").append(escapeJson(rs.getString("title"))).append("\",");
            json.append("\"date\":\"").append(rs.getDate("date")).append("\",");
            json.append("\"status\":\"").append(escapeJson(rs.getString("status"))).append("\"");
            json.append("}");
        }
        
        rs.close();
        stmt.close();
        json.append("]");
        return json.toString();
    }
    
    private String getBorrowedBookIds(Connection conn, int userId) throws Exception {
        StringBuilder json = new StringBuilder("[");
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT book_id FROM borrows WHERE user_id = ? AND status = 'borrowed'"
        );
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        
        boolean first = true;
        while (rs.next()) {
            if (!first) json.append(",");
            first = false;
            json.append(rs.getInt("book_id"));
        }
        
        rs.close();
        stmt.close();
        json.append("]");
        return json.toString();
    }
    
    private String getUserReservations(Connection conn, int userId) throws Exception {
        StringBuilder json = new StringBuilder("[");
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT r.reservation_id, bk.title, bk.author, r.reservation_date, r.status, r.queue_position " +
            "FROM reservations r JOIN books bk ON r.book_id = bk.book_id " +
            "WHERE r.user_id = ? ORDER BY r.reservation_date DESC"
        );
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        
        boolean first = true;
        while (rs.next()) {
            if (!first) json.append(",");
            first = false;
            
            json.append("{");
            json.append("\"id\":").append(rs.getInt("reservation_id")).append(",");
            json.append("\"title\":\"").append(escapeJson(rs.getString("title"))).append("\",");
            json.append("\"author\":\"").append(escapeJson(rs.getString("author"))).append("\",");
            json.append("\"reserved_date\":\"").append(rs.getDate("reservation_date")).append("\",");
            json.append("\"status\":\"").append(escapeJson(rs.getString("status"))).append("\",");
            json.append("\"queue_position\":").append(rs.getInt("queue_position"));
            json.append("}");
        }
        
        rs.close();
        stmt.close();
        json.append("]");
        return json.toString();
    }
    
    private String getUserFines(Connection conn, int userId) throws Exception {
        StringBuilder json = new StringBuilder("[");
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT f.fine_id, f.amount, f.reason, f.status, f.paid_date, bk.title, bk.author " +
            "FROM fines f LEFT JOIN borrows br ON f.borrow_id = br.borrow_id LEFT JOIN books bk ON br.book_id = bk.book_id " +
            "WHERE f.user_id = ? ORDER BY f.paid_date DESC NULLS LAST"
        );
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        
        boolean first = true;
        while (rs.next()) {
            if (!first) json.append(",");
            first = false;
            
            json.append("{");
            json.append("\"id\":").append(rs.getInt("fine_id")).append(",");
            json.append("\"title\":\"").append(escapeJson(rs.getString("title") != null ? rs.getString("title") : "")).append("\",");
            json.append("\"author\":\"").append(escapeJson(rs.getString("author") != null ? rs.getString("author") : "")).append("\",");
            json.append("\"amount\":").append(rs.getDouble("amount")).append(",");
            json.append("\"reason\":\"").append(escapeJson(rs.getString("reason") != null ? rs.getString("reason") : "")).append("\",");
            json.append("\"status\":\"").append(escapeJson(rs.getString("status"))).append("\",");
            json.append("\"date\":\"").append(rs.getDate("paid_date")).append("\"");
            json.append("}");
        }
        
        rs.close();
        stmt.close();
        json.append("]");
        return json.toString();
    }
}
