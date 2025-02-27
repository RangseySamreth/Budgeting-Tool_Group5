package com.budget.tool;

import java.sql.*;

public class LoginUser {
    private static final String DB_URL = "jdbc:sqlite:budgeting.db"; // Database connection URL

    public boolean userExists(String username) {
        String sql = "SELECT name FROM budgeting WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking user existence: " + e.getMessage());
        }
        return false;
    }

    public boolean registerUser(String username, String password) {
        String sql = "INSERT INTO budgeting (name, password, balance) VALUES (?, ?, 0)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // Store plaintext for now, but consider hashing
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Database error during registration: " + e.getMessage());
        }
        return false;
    }

    public boolean login(String username, String password) {
        String sql = "SELECT password FROM budgeting WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }
}

