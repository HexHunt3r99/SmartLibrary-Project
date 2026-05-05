package com.smartlibrary.controller;

import com.smartlibrary.util.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("full_name");
        
        try {
            Connection conn = DBConnection.getConnection();
            
            String sql = "INSERT INTO users (username, email, password, full_name, role) VALUES (?, ?, ?, ?, 'student')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, fullName);
            
            stmt.executeUpdate();
            
            stmt.close();
            conn.close();
            
            response.sendRedirect("login.jsp");
            
        } catch (Exception e) {
            response.sendRedirect("register.jsp?error=1");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("register.jsp");
    }
}
