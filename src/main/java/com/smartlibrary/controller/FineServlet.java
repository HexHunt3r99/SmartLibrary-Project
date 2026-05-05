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

public class FineServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        if (userId == null) {
            out.print("{\"success\":false,\"message\":\"Please login first\"}");
            return;
        }
        
        String action = request.getParameter("action");
        
        if (action == null || !action.equals("pay")) {
            out.print("{\"success\":false,\"message\":\"Invalid action\"}");
            return;
        }
        
        String role = (String) session.getAttribute("role");
        boolean isAdmin = "admin".equals(role);
        
        try {
            int fineId = Integer.parseInt(request.getParameter("fine_id"));
            
            Connection conn = DBConnection.getConnection();
            
            if (isAdmin) {
                String updateSql = "UPDATE fines SET status = 'paid', paid_date = CURRENT_TIMESTAMP WHERE fine_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, fineId);
                int updated = updateStmt.executeUpdate();
                updateStmt.close();
                
                if (updated > 0) {
                    out.print("{\"success\":true,\"message\":\"Fine paid successfully!\"}");
                } else {
                    out.print("{\"success\":false,\"message\":\"Fine not found\"}");
                }
            } else {
                String checkSql = "SELECT fine_id, status FROM fines WHERE fine_id = ? AND user_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setInt(1, fineId);
                checkStmt.setInt(2, userId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (!rs.next()) {
                    rs.close();
                    checkStmt.close();
                    out.print("{\"success\":false,\"message\":\"Fine not found\"}");
                    return;
                }
                
                String status = rs.getString("status");
                rs.close();
                checkStmt.close();
                
                if (status.equals("paid")) {
                    out.print("{\"success\":false,\"message\":\"This fine is already paid\"}");
                    return;
                }
                
                String updateSql = "UPDATE fines SET status = 'paid', paid_date = CURRENT_TIMESTAMP WHERE fine_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, fineId);
                updateStmt.executeUpdate();
                updateStmt.close();
                
                out.print("{\"success\":true,\"message\":\"Fine paid successfully!\"}");
            }
            
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("user/fines.jsp");
    }
}
