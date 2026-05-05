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

public class AdminStatsServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        String role = (String) session.getAttribute("role");
        
        if (userId == null || !RoleUtils.canManageLibrary(role)) {
            out.print("{\"success\":false,\"message\":\"Access denied\"}");
            return;
        }
        
        try {
            Connection conn = DBConnection.getConnection();
            
            int totalUsers = getCount(conn, "SELECT COUNT(*) FROM users");
            int totalBooks = getCount(conn, "SELECT COUNT(*) FROM books");
            int availableBooks = getCount(conn, "SELECT COALESCE(SUM(available), 0) FROM books");
            int activeBorrows = getCount(conn, "SELECT COUNT(*) FROM borrows WHERE status = 'borrowed'");
            int totalReservations = getCount(conn, "SELECT COUNT(*) FROM reservations");
            
            int totalStudents = getCount(conn, "SELECT COUNT(*) FROM users WHERE role = 'student'");
            int totalFaculty = getCount(conn, "SELECT COUNT(*) FROM users WHERE role = 'faculty'");
            int totalLibrarians = getCount(conn, "SELECT COUNT(*) FROM users WHERE role = 'librarian'");
            int totalAdmins = getCount(conn, "SELECT COUNT(*) FROM users WHERE role = 'admin'");
            
            int totalBorrows = getCount(conn, "SELECT COUNT(*) FROM borrows");
            int returnedBorrows = getCount(conn, "SELECT COUNT(*) FROM borrows WHERE status = 'returned'");
            int overdueBorrows = getCount(conn, "SELECT COUNT(*) FROM borrows WHERE status = 'overdue'");
            
            int activeReservations = getCount(conn, "SELECT COUNT(*) FROM reservations WHERE status = 'pending'");
            int readyPickup = getCount(conn, "SELECT COUNT(*) FROM reservations WHERE status = 'ready'");
            int pendingApproval = getCount(conn, "SELECT COUNT(*) FROM reservations WHERE status = 'pending'");
            int completedReservations = getCount(conn, "SELECT COUNT(*) FROM reservations WHERE status = 'completed'");
            
            double totalFinesAmount = getSum(conn, "SELECT COALESCE(SUM(amount), 0) FROM fines");
            double pendingFinesAmount = getSum(conn, "SELECT COALESCE(SUM(amount), 0) FROM fines WHERE status = 'unpaid'");
            double collectedFinesAmount = getSum(conn, "SELECT COALESCE(SUM(amount), 0) FROM fines WHERE status = 'paid'");
            int usersWithFines = getCount(conn, "SELECT COUNT(DISTINCT user_id) FROM fines WHERE status = 'unpaid'");
            
            out.print("{");
            out.print("\"success\":true,");
            out.print("\"total_users\":" + totalUsers + ",");
            out.print("\"total_books\":" + totalBooks + ",");
            out.print("\"available_books\":" + availableBooks + ",");
            out.print("\"active_borrows\":" + activeBorrows + ",");
            out.print("\"total_reservations\":" + totalReservations + ",");
            out.print("\"total_students\":" + totalStudents + ",");
            out.print("\"total_faculty\":" + totalFaculty + ",");
            out.print("\"total_librarians\":" + totalLibrarians + ",");
            out.print("\"total_admins\":" + totalAdmins + ",");
            out.print("\"total_borrows\":" + totalBorrows + ",");
            out.print("\"returned_borrows\":" + returnedBorrows + ",");
            out.print("\"overdue_borrows\":" + overdueBorrows + ",");
            out.print("\"active_reservations\":" + activeReservations + ",");
            out.print("\"ready_pickup\":" + readyPickup + ",");
            out.print("\"pending_approval\":" + pendingApproval + ",");
            out.print("\"completed_reservations\":" + completedReservations + ",");
            out.print("\"total_fines\":" + totalFinesAmount + ",");
            out.print("\"pending_fines\":" + pendingFinesAmount + ",");
            out.print("\"collected_fines\":" + collectedFinesAmount + ",");
            out.print("\"users_with_fines\":" + usersWithFines);
            out.print("}");
            
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}");
        }
    }
    
    private int getCount(Connection conn, String sql) throws Exception {
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        int count = rs.next() ? rs.getInt(1) : 0;
        rs.close();
        stmt.close();
        return count;
    }
    
    private double getSum(Connection conn, String sql) throws Exception {
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        double sum = rs.next() ? rs.getDouble(1) : 0;
        rs.close();
        stmt.close();
        return sum;
    }
}
