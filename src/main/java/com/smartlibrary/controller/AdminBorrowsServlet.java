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

public class AdminBorrowsServlet extends HttpServlet {
    
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
            
            String sql = "SELECT bo.borrow_id, bo.borrow_date, bo.due_date, bo.return_date, bo.status, " +
                "u.user_id, u.full_name, u.username, b.book_id, b.title " +
                "FROM borrows bo " +
                "JOIN users u ON bo.user_id = u.user_id " +
                "JOIN books b ON bo.book_id = b.book_id";
            
            if (status != null && !status.isEmpty() && !"All Status".equals(status)) {
                sql += " WHERE bo.status = '" + status.toLowerCase() + "'";
            }
            
            sql += " ORDER BY bo.borrow_date DESC";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            out.print("[");
            boolean first = true;
            while (rs.next()) {
                if (!first) out.print(",");
                first = false;
                out.print("{");
                out.print("\"borrow_id\":" + rs.getInt("borrow_id") + ",");
                out.print("\"user_id\":" + rs.getInt("user_id") + ",");
                out.print("\"full_name\":\"" + escape(rs.getString("full_name")) + "\",");
                out.print("\"username\":\"" + escape(rs.getString("username")) + "\",");
                out.print("\"book_id\":" + rs.getInt("book_id") + ",");
                out.print("\"title\":\"" + escape(rs.getString("title")) + "\",");
                out.print("\"borrow_date\":\"" + rs.getTimestamp("borrow_date") + "\",");
                out.print("\"due_date\":\"" + rs.getTimestamp("due_date") + "\",");
                String returnDate = rs.getTimestamp("return_date") != null ? "\"" + rs.getTimestamp("return_date") + "\"" : "null";
                out.print("\"return_date\":" + returnDate + ",");
                out.print("\"status\":\"" + rs.getString("status") + "\"");
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
            int borrowId = Integer.parseInt(request.getParameter("borrow_id"));
            Connection conn = DBConnection.getConnection();
            
            if ("return".equals(action)) {
                String sql = "UPDATE borrows SET status = 'returned', return_date = CURRENT_TIMESTAMP WHERE borrow_id = ? AND status = 'borrowed'";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, borrowId);
                int updated = stmt.executeUpdate();
                stmt.close();
                
                if (updated > 0) {
                    out.print("{\"success\":true,\"message\":\"Book returned successfully!\"}");
                } else {
                    out.print("{\"success\":false,\"message\":\"Cannot return this borrow\"}");
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
