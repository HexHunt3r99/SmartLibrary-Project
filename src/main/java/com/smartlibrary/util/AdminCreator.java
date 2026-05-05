package com.smartlibrary.util;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdminCreator {

    public static void main(String[] args) {
        String username = "admin";
        String email = "admin@smartlibrary.com";
        String password = "admin123";
        String fullName = "Administrator";

        try {
            Connection conn = DBConnection.getConnection();
            
            String sql = "INSERT INTO users (username, email, password, full_name, role, status) " + "VALUES (?, ?, ?, ?, 'admin', 'active')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, fullName);
            
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            
            System.out.println("Admin created successfully!");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

// This file is only for creating admin account. I didn't add admin role option in the registration page because anyone could register as admin which is not safe. 
// Regular users will automatically get "user" role.