package com.budget.tool;

import java.sql.*;

public class LoginUser {
    private static final String DB_URL = "jdbc:sqlite:budgeting.db";
    private SecurityUtils securityUtils;

    public LoginUser() {
        securityUtils = new SecurityUtils();
    }

    public boolean userExists(String username) {
        String sql = "SELECT name FROM budgeting WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Database error while checking user existence: " + e.getMessage());
        }
        return false;
    }

    public boolean registerUser(String username, String password) {
        if (userExists(username)) {
            System.out.println("User already exists. Please log in.");
            return false;
        }

        String hashedPassword = securityUtils.hashPassword(password);
        String sql = "INSERT INTO budgeting (name, password, balance) VALUES (?, ?, 0)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.executeUpdate();
            System.out.println("User registered successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Database error during registration: " + e.getMessage());
        }
        return false;
    }

    public boolean login(String username, String password) {
        String sql = "SELECT id, password FROM budgeting WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                String storedPassword = rs.getString("password");
                if (securityUtils.verifyPassword(password, storedPassword)) {
                    SessionManager.setCurrentUser(userId, username); // Store session correctly
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }

}
