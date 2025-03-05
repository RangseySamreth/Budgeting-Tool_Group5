package com.budget.tool;

import java.sql.*;
import java.time.LocalDate;

public class SavingManager {
    private static final String DB_URL = "jdbc:sqlite:budgeting.db";

    // Method to add savings to the user's account
    public static void addSavings(int userId, double amountToSave) {
        try {
            // Insert savings into the savings table
            insertSavings(userId, amountToSave);
            System.out.println("Savings of $" + amountToSave + " added for user " + userId);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Insert new savings record into the savings table
    private static void insertSavings(int userId, double savedAmount) throws SQLException {
        String sql = "INSERT INTO savings (user_id, saved_amount, date_saved) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDouble(2, savedAmount);
            pstmt.setString(3, LocalDate.now().toString()); 
            pstmt.executeUpdate();
        }
    }

    // Retrieve the total savings for a user by summing all the saved amounts
    public static double getTotalSavings(int userId) {
        double totalSavings = 0;
        String sql = "SELECT SUM(saved_amount) AS total_savings FROM savings WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalSavings = rs.getDouble("total_savings");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return totalSavings;
    }

    // Display the total savings for a user
    public static void displaySavingHistory(int userId) {
        String sql = "SELECT saved_amount, date_saved FROM savings WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("------------------------------------------------");
            System.out.printf("%-10s | %-15s\n", "Amount", "Date Saved");
            System.out.println("------------------------------------------------");

            while (rs.next()) {
                double amount = rs.getDouble("saved_amount");
                String dateSaved = rs.getString("date_saved");
                System.out.printf("%-10.2f | %-15s\n", amount, dateSaved);
            }

            System.out.println("------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Database error while retrieving savings history: " + e.getMessage());
        }
    }

}
