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

public class ProfileServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        
        if (userId == null) {
            out.print("{\"success\":false,\"message\":\"Not logged in\"}");
            return;
        }
        
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT user_id, username, email, full_name, role FROM users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                out.print("{");
                out.print("\"success\":true,");
                out.print("\"user_id\":" + rs.getInt("user_id") + ",");
                out.print("\"username\":\"" + escapeJson(rs.getString("username")) + "\",");
                out.print("\"email\":\"" + escapeJson(rs.getString("email")) + "\",");
                out.print("\"full_name\":\"" + escapeJson(rs.getString("full_name")) + "\",");
                out.print("\"role\":\"" + rs.getString("role") + "\"");
                out.print("}");
            } else {
                out.print("{\"success\":false,\"message\":\"User not found\"}");
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        
        if (userId == null) {
            out.print("{\"success\":false,\"message\":\"Not logged in\"}");
            return;
        }
        
        String action = request.getParameter("action");
        String email = request.getParameter("email");
        String fullName = request.getParameter("full_name");
        String newPassword = request.getParameter("new_password");
        
        try {
            Connection conn = DBConnection.getConnection();
            
            if ("updateProfile".equals(action)) {
                String sql = "UPDATE users SET email = ?, full_name = ? WHERE user_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, email);
                stmt.setString(2, fullName);
                stmt.setInt(3, userId);
                stmt.executeUpdate();
                stmt.close();
                conn.close();
                out.print("{\"success\":true,\"message\":\"Profile updated successfully\"}");
            } 
            else if ("changePassword".equals(action)) {
                String updateSql = "UPDATE users SET password = ? WHERE user_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, newPassword);
                updateStmt.setInt(2, userId);
                updateStmt.executeUpdate();
                updateStmt.close();
                out.print("{\"success\":true,\"message\":\"Password changed successfully\"}");
            } else {
                out.print("{\"success\":false,\"message\":\"Invalid action\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}");
        }
    }
    
    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
