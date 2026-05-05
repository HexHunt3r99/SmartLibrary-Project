package com.smartlibrary.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/smartlibrary";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "admin";
    
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
