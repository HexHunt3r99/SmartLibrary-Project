package com.smartlibrary.controller;

import com.smartlibrary.util.DBConnection;
import com.smartlibrary.util.RoleUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class RenewBookServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        String role = (String) session.getAttribute("role");
        
        if (userId == null) {
            response.getWriter().print("{\"success\":false,\"message\":\"Please login first\"}");
            return;
        }
        
        String borrowIdStr = request.getParameter("borrow_id");
        String daysStr = request.getParameter("days");
        System.out.println("RenewBookServlet - borrow_id: " + borrowIdStr + ", days: " + daysStr);
        if (borrowIdStr == null || borrowIdStr.isEmpty()) {
            response.getWriter().print("{\"success\":false,\"message\":\"Invalid request\"}");
            return;
        }
        
        int days = RoleUtils.getRenewDays(role);
        if (daysStr != null && !daysStr.isEmpty()) {
            try {
                days = Integer.parseInt(daysStr);
            } catch (NumberFormatException e) {
                days = RoleUtils.getRenewDays(role);
            }
        }
        
        try {
            int borrowId = Integer.parseInt(borrowIdStr);
            Connection conn = DBConnection.getConnection();
            
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE borrows SET due_date = due_date + INTERVAL '1 day' * ? " +
                "WHERE borrow_id = ? AND user_id = ? AND status = 'borrowed'"
            );
            stmt.setInt(1, days);
            stmt.setInt(2, borrowId);
            stmt.setInt(3, userId);
            
            int updated = stmt.executeUpdate();
            stmt.close();
            conn.close();
            
            if (updated > 0) {
                response.getWriter().print("{\"success\":true,\"message\":\"Book renewed successfully!\"}");
            } else {
                response.getWriter().print("{\"success\":false,\"message\":\"Cannot renew this book\"}");
            }
            
        } catch (Exception e) {
            response.getWriter().print("{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}");
        }
    }
}
