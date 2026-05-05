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

public class BookServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        String action = request.getParameter("action");
        
        if (idParam != null && !idParam.isEmpty()) {
            viewBook(request, response);
            return;
        }
        
        if (action == null) {
            action = "list";
        }
        
        if (action.equals("search")) {
            searchBooks(request, response);
        } else {
            listBooks(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendRedirect("admin/books.jsp");
            return;
        }
        
        if (action.equals("add")) {
            addBook(request, response);
        } else if (action.equals("update")) {
            updateBook(request, response);
        } else if (action.equals("delete")) {
            deleteBook(request, response);
        }
    }
    
    private void listBooks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String sql = "SELECT book_id, title, author, publisher, publish_year, category, quantity, available FROM books ORDER BY title";
        
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            out.print("[");
            boolean first = true;
            
            while (rs.next()) {
                if (!first) out.print(",");
                
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                String category = rs.getString("category");
                
                out.print("{");
                out.print("\"book_id\":" + rs.getInt("book_id") + ",");
                out.print("\"title\":\"" + escapeJson(title) + "\",");
                out.print("\"author\":\"" + escapeJson(author) + "\",");
                out.print("\"publisher\":\"" + escapeJson(publisher) + "\",");
                out.print("\"publish_year\":" + rs.getInt("publish_year") + ",");
                out.print("\"category\":\"" + escapeJson(category) + "\",");
                out.print("\"quantity\":" + rs.getInt("quantity") + ",");
                out.print("\"available\":" + rs.getInt("available"));
                out.print("}");
                
                first = false;
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
    
    private void searchBooks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String query = request.getParameter("query");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String sql = "SELECT book_id, title, author, category, available FROM books WHERE title LIKE ? OR author LIKE ?";
        
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            String term = "%" + query + "%";
            stmt.setString(1, term);
            stmt.setString(2, term);
            
            ResultSet rs = stmt.executeQuery();
            
            out.print("[");
            boolean first = true;
            
            while (rs.next()) {
                if (!first) out.print(",");
                out.print("{");
                out.print("\"book_id\":" + rs.getInt("book_id") + ",");
                out.print("\"title\":\"" + escapeJson(rs.getString("title")) + "\",");
                out.print("\"author\":\"" + escapeJson(rs.getString("author")) + "\",");
                out.print("\"available\":" + rs.getInt("available"));
                out.print("}");
                first = false;
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
    
    private void viewBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int bookId = Integer.parseInt(request.getParameter("id"));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String sql = "SELECT book_id, title, author, publisher, publish_year, category, quantity, available FROM books WHERE book_id = ?";
        
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                out.print("{\"book\":{");
                out.print("\"book_id\":" + rs.getInt("book_id") + ",");
                out.print("\"title\":\"" + escapeJson(rs.getString("title")) + "\",");
                out.print("\"author\":\"" + escapeJson(rs.getString("author")) + "\",");
                out.print("\"publisher\":\"" + escapeJson(rs.getString("publisher")) + "\",");
                out.print("\"publish_year\":" + rs.getInt("publish_year") + ",");
                out.print("\"category\":\"" + escapeJson(rs.getString("category")) + "\",");
                out.print("\"quantity\":" + rs.getInt("quantity") + ",");
                out.print("\"available\":" + rs.getInt("available"));
                out.print("}}");
            } else {
                out.print("{\"book\":null}");
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"book\":null}");
        }
    }
    
    private void addBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        if (!RoleUtils.canManageLibrary(role)) {
            out.print("{\"success\":false,\"message\":\"Access denied\"}");
            return;
        }
        
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        int publishYear = Integer.parseInt(request.getParameter("publish_year"));
        String category = request.getParameter("category");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        String sql = "INSERT INTO books (title, author, publisher, publish_year, category, quantity, available) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try {
            Connection conn = DBConnection.getConnection();
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
            
            out.print("{\"success\":true,\"message\":\"Book added successfully\"}");
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}");
        }
    }
    
    private void updateBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        if (!RoleUtils.canManageLibrary(role)) {
            out.print("{\"success\":false,\"message\":\"Access denied\"}");
            return;
        }
        
        int bookId = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        String sql = "UPDATE books SET title = ?, author = ?, quantity = ? WHERE book_id = ?";
        
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setInt(3, quantity);
            stmt.setInt(4, bookId);
            
            stmt.executeUpdate();
            
            stmt.close();
            conn.close();
            
            out.print("{\"success\":true,\"message\":\"Book updated successfully\"}");
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}");
        }
    }
    
    private void deleteBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        if (!RoleUtils.canManageLibrary(role)) {
            out.print("{\"success\":false,\"message\":\"Access denied\"}");
            return;
        }
        
        int bookId = Integer.parseInt(request.getParameter("id"));
        String sql = "DELETE FROM books WHERE book_id = ?";
        
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
            
            stmt.close();
            conn.close();
            
            out.print("{\"success\":true,\"message\":\"Book deleted successfully\"}");
            
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

