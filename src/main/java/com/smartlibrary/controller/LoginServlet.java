package com.smartlibrary.controller;

import com.smartlibrary.util.DBConnection;
import com.smartlibrary.util.RoleUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            Connection conn = DBConnection.getConnection();
            
            if (conn == null) {
                response.sendRedirect("login.jsp?error=1");
                return;
            }
            
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String role = rs.getString("role");
                
                HttpSession session = request.getSession();
                session.setAttribute("user_id", rs.getInt("user_id"));
                session.setAttribute("username", rs.getString("username"));
                session.setAttribute("role", role);
                
                rs.close();
                stmt.close();
                conn.close();
                
                if (RoleUtils.isAdmin(role)) {
                    response.sendRedirect("admin/dashboard.jsp");
                } else if (RoleUtils.isLibrarian(role)) {
                    response.sendRedirect("admin/books.jsp");
                } else {
                    response.sendRedirect("user/dashboard.jsp");
                }
            } else {
                rs.close();
                stmt.close();
                conn.close();
                response.sendRedirect("login.jsp?error=1");
            }
            
        } catch (Exception e) {
            response.sendRedirect("login.jsp?error=1");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}

