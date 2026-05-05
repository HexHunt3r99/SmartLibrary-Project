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

public class RecentActivityServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        String role = (String) session.getAttribute("role");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        if (userId == null || role == null || !"admin".equals(role)) {
            out.print("[]");
            return;
        }
        
        try {
            Connection conn = DBConnection.getConnection();
            
            String sql = 
                "(SELECT 'user' as type, u.user_id as id, u.full_name, u.email as detail, u.created_at as timestamp, 'New User Registered' as title " +
                "FROM users u ORDER BY u.created_at DESC LIMIT 5) " +
                "UNION ALL " +
                "(SELECT 'borrow' as type, bo.borrow_id as id, u.full_name, b.title as detail, bo.borrow_date as timestamp, 'Book Borrowed' as title " +
                "FROM borrows bo JOIN users u ON bo.user_id = u.user_id JOIN books b ON bo.book_id = b.book_id " +
                "ORDER BY bo.borrow_date DESC LIMIT 5) " +
                "UNION ALL " +
                "(SELECT 'return' as type, bo.borrow_id as id, u.full_name, b.title as detail, bo.return_date as timestamp, 'Book Returned' as title " +
                "FROM borrows bo JOIN users u ON bo.user_id = u.user_id JOIN books b ON bo.book_id = b.book_id " +
                "WHERE bo.return_date IS NOT NULL ORDER BY bo.return_date DESC LIMIT 5) " +
                "UNION ALL " +
                "(SELECT 'reservation' as type, r.reservation_id as id, u.full_name, b.title as detail, r.reservation_date as timestamp, 'New Reservation' as title " +
                "FROM reservations r JOIN users u ON r.user_id = u.user_id JOIN books b ON r.book_id = b.book_id " +
                "ORDER BY r.reservation_date DESC LIMIT 5) " +
                "ORDER BY timestamp DESC LIMIT 10";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            out.print("[");
            boolean first = true;
            while (rs.next()) {
                if (!first) out.print(",");
                first = false;
                out.print("{");
                out.print("\"type\":\"" + rs.getString("type") + "\",");
                out.print("\"title\":\"" + escape(rs.getString("title")) + "\",");
                out.print("\"full_name\":\"" + escape(rs.getString("full_name")) + "\",");
                out.print("\"detail\":\"" + escape(rs.getString("detail")) + "\",");
                out.print("\"timestamp\":\"" + rs.getTimestamp("timestamp") + "\"");
                out.print("}");
            }
            out.print("]");
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("[]");
        }
    }
    
    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
