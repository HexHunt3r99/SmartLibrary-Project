package com.smartlibrary.controller;

import com.smartlibrary.util.DBConnection;
import com.smartlibrary.util.RoleUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AddBookServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        
        if (!RoleUtils.canManageLibrary(role)) {
            out.print("{\"success\":false,\"message\":\"Only admin or librarian can manage books\"}");
            return;
        }
        
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        String publishYearStr = request.getParameter("publish_year");
        String category = request.getParameter("category");
        String quantityStr = request.getParameter("quantity");
        
        if (title == null || author == null || publisher == null || 
            publishYearStr == null || category == null || quantityStr == null) {
            out.print("{\"success\":false,\"message\":\"Missing required fields\"}");
            return;
        }
        
        try {
            int publishYear = Integer.parseInt(publishYearStr);
            int quantity = Integer.parseInt(quantityStr);
            
            Connection conn = DBConnection.getConnection();
            
            if (conn == null) {
                out.print("{\"success\":false,\"message\":\"Database connection failed\"}");
                return;
            }
            
            String sql = "INSERT INTO books (title, author, publisher, publish_year, category, quantity, available) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, publisher);
            stmt.setInt(4, publishYear);
            stmt.setString(5, category);
            stmt.setInt(6, quantity);
            stmt.setInt(7, quantity);
            
            stmt.executeUpdate();
            
            stmt.close();
            conn.close();
            
            out.print("{\"success\":true,\"message\":\"Book added successfully!\"}");
            
        } catch (NumberFormatException e) {
            out.print("{\"success\":false,\"message\":\"Invalid number format\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("admin/books.jsp");
    }
}
