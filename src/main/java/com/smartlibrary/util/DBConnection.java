package com.smartlibrary.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    private static final String DB_URL = "jdbc:postgresql://ep-odd-truth-anwxhxc8-pooler.c-6.us-east-1.aws.neon.tech/neondb?sslmode=require";
    private static final String DB_USER = "neondb_owner";
    private static final String DB_PASS = "npg_JvNTL0w8SVjO";
    
    public static Connection getConnection() {
        Connection conn = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
        
        return conn;
    }
    
    public static void main(String[] args) {
        Connection conn = getConnection();
        
        if (conn != null) {
            System.out.println("Connected to database!");
            try {
                conn.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        } else {
            System.out.println("Connection failed!");
        }
    }
}
