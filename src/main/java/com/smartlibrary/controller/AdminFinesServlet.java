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

public class AdminFinesServlet extends HttpServlet {
    
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
        
        String status = request.getParameter("status");
        
        try {
            Connection conn = DBConnection.getConnection();
            
            String sql = "SELECT f.fine_id, f.amount, f.reason, f.status, f.paid_date, " +
                "f.created_at, u.user_id, u.full_name, u.username, b.book_id, b.title " +
                "FROM fines f " +
                "JOIN users u ON f.user_id = u.user_id " +
                "LEFT JOIN borrows bo ON f.borrow_id = bo.borrow_id " +
                "LEFT JOIN books b ON bo.book_id = b.book_id OR f.book_id = b.book_id";
            
            if (status != null && !status.isEmpty() && !"All Status".equals(status)) {
                sql += " WHERE f.status = '" + status.toLowerCase() + "'";
            }
            
            sql += " ORDER BY f.created_at DESC";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            out.print("[");
            boolean first = true;
            while (rs.next()) {
                if (!first) out.print(",");
                first = false;
                out.print("{");
                out.print("\"fine_id\":" + rs.getInt("fine_id") + ",");
                out.print("\"user_id\":" + rs.getInt("user_id") + ",");
                out.print("\"full_name\":\"" + escape(rs.getString("full_name")) + "\",");
                out.print("\"username\":\"" + escape(rs.getString("username")) + "\",");
                out.print("\"book_id\":" + rs.getInt("book_id") + ",");
                out.print("\"title\":\"" + escape(rs.getString("title")) + "\",");
                out.print("\"reason\":\"" + escape(rs.getString("reason")) + "\",");
                out.print("\"amount\":" + rs.getDouble("amount") + ",");
                out.print("\"status\":\"" + rs.getString("status") + "\",");
                out.print("\"created_at\":\"" + rs.getTimestamp("created_at") + "\"");
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
