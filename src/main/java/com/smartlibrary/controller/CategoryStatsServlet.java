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

public class CategoryStatsServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        if (userId == null) {
            out.print("[]");
            return;
        }
        
        try {
            Connection conn = DBConnection.getConnection();
            
            String sql = "SELECT category, COUNT(*) as count FROM books WHERE category IS NOT NULL GROUP BY category ORDER BY count DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            out.print("[");
            boolean first = true;
            while (rs.next()) {
                if (!first) out.print(",");
                first = false;
                out.print("{\"category\":\"" + rs.getString("category") + "\",\"count\":" + rs.getInt("count") + "}");
            }
            out.print("]");
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            out.print("[]");
        }
    }
}
