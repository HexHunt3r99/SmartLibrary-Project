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

public class UserServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String action = request.getParameter("action");
        String search = request.getParameter("search");
        
        try {
            Connection conn = DBConnection.getConnection();
            
            String sql = "SELECT user_id, username, email, full_name, role, status FROM users";
            
            if (search != null && !search.isEmpty()) {
                sql += " WHERE username LIKE ? OR email LIKE ? OR full_name LIKE ?";
            }
            
            sql += " ORDER BY user_id DESC";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            if (search != null && !search.isEmpty()) {
                String term = "%" + search + "%";
                stmt.setString(1, term);
                stmt.setString(2, term);
                stmt.setString(3, term);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            out.print("[");
            boolean first = true;
            while (rs.next()) {
                if (!first) out.print(",");
                out.print("{");
                out.print("\"user_id\":" + rs.getInt("user_id") + ",");
                out.print("\"username\":\"" + rs.getString("username") + "\",");
                out.print("\"email\":\"" + rs.getString("email") + "\",");
                out.print("\"full_name\":\"" + rs.getString("full_name") + "\",");
                out.print("\"role\":\"" + rs.getString("role") + "\",");
                out.print("\"status\":\"" + rs.getString("status") + "\"");
                out.print("}");
                first = false;
            }
            out.print("]");
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            out.print("[]");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String action = request.getParameter("action");
        
        if ("delete".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("id"));
            
            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE user_id = ?");
                stmt.setInt(1, userId);
                stmt.executeUpdate();
                stmt.close();
                conn.close();
                
                out.print("{\"success\":true,\"message\":\"User deleted\"}");
            } catch (Exception e) {
                out.print("{\"success\":false,\"message\":\"Error\"}");
            }
        } else {
            out.print("{\"success\":false,\"message\":\"Invalid action\"}");
        }
    }
}