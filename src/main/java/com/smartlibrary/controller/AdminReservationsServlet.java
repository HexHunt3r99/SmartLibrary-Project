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

public class AdminReservationsServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        String role = (String) session.getAttribute("role");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        if (userId == null || !RoleUtils.canManageLibrary(role)) {
            out.print("[]");
            return;
        }
        
        String status = request.getParameter("status");
        
        try {
            Connection conn = DBConnection.getConnection();
            
            String sql = "SELECT r.reservation_id, r.reservation_date, r.expiration_date, r.status, r.queue_position, " +
                "u.user_id, u.full_name, u.username, b.book_id, b.title " +
                "FROM reservations r " +
                "JOIN users u ON r.user_id = u.user_id " +
                "JOIN books b ON r.book_id = b.book_id";
            
            if (status != null && !status.isEmpty() && !"All Status".equals(status)) {
                sql += " WHERE r.status = '" + status.toLowerCase() + "'";
            }
            
            sql += " ORDER BY r.reservation_date DESC";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            out.print("[");
            boolean first = true;
            while (rs.next()) {
                if (!first) out.print(",");
                first = false;
                out.print("{");
                out.print("\"reservation_id\":" + rs.getInt("reservation_id") + ",");
                out.print("\"user_id\":" + rs.getInt("user_id") + ",");
                out.print("\"full_name\":\"" + escape(rs.getString("full_name")) + "\",");
                out.print("\"username\":\"" + escape(rs.getString("username")) + "\",");
                out.print("\"book_id\":" + rs.getInt("book_id") + ",");
                out.print("\"title\":\"" + escape(rs.getString("title")) + "\",");
                out.print("\"reservation_date\":\"" + rs.getTimestamp("reservation_date") + "\",");
                out.print("\"expiration_date\":\"" + rs.getTimestamp("expiration_date") + "\",");
                out.print("\"status\":\"" + rs.getString("status") + "\",");
                out.print("\"queue_position\":" + rs.getInt("queue_position"));
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
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        String role = (String) session.getAttribute("role");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        if (userId == null || !RoleUtils.canManageLibrary(role)) {
            out.print("{\"success\":false,\"message\":\"Access denied\"}");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            int reservationId = Integer.parseInt(request.getParameter("reservation_id"));
            Connection conn = DBConnection.getConnection();
            
            if ("approve".equals(action)) {
                String sql = "UPDATE reservations SET status = 'ready' WHERE reservation_id = ? AND status = 'pending'";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, reservationId);
                int updated = stmt.executeUpdate();
                stmt.close();
                
                if (updated > 0) {
                    out.print("{\"success\":true,\"message\":\"Reservation approved!\"}");
                } else {
                    out.print("{\"success\":false,\"message\":\"Cannot approve this reservation\"}");
                }
            } else if ("cancel".equals(action)) {
                String sql = "UPDATE reservations SET status = 'cancelled' WHERE reservation_id = ? AND status IN ('pending', 'active')";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, reservationId);
                int updated = stmt.executeUpdate();
                stmt.close();
                
                if (updated > 0) {
                    out.print("{\"success\":true,\"message\":\"Reservation cancelled!\"}");
                } else {
                    out.print("{\"success\":false,\"message\":\"Cannot cancel this reservation\"}");
                }
            } else {
                out.print("{\"success\":false,\"message\":\"Invalid action\"}");
            }
            
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}");
        }
    }
    
    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
