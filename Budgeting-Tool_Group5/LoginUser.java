package com.budget.tool;

import java.sql.*;
import java.util.Scanner;

public class LoginUser {
    private SecurityUtils securityUtils;
    private static final String DB_URL = "jdbc:sqlite:budgeting.db"; // Database connection URL
    private static Scanner scanner = new Scanner(System.in);

    public LoginUser() {
        securityUtils = new SecurityUtils();
    }

    public boolean login(String username, String password) {
        String encryptedPassword = securityUtils.hashPassword(password);
        if (checkUserCredentials(username, encryptedPassword)) {
            System.out.println("Login successful! Welcome " + username + ".");
            return true;
        }
        return false;
    }

    private boolean checkUserCredentials(String username, String password) {
        String sql = "SELECT password FROM budgeting WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password); // Compare hashed passwords
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }

    public void registerUser(String username, String password) {
        String encryptedPassword = securityUtils.hashPassword(password);
        String sql = "INSERT INTO budgeting (name, password, balance) VALUES (?, ?, 0)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, encryptedPassword);
            pstmt.executeUpdate();
            System.out.println("User registered successfully. Please log in now.");
        } catch (SQLException e) {
            System.out.println("Database error during registration: " + e.getMessage());
        }
    }

    public void start() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        if (!userExists(username)) {
            System.out.println("User does not exist. Proceeding with registration.");
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            registerUser(username, password);
        } else {
            System.out.println("User already exists. Please log in.");
        }

        System.out.print("Enter password to log in: ");
        String password = scanner.nextLine();
        if (login(username, password)) {
            manageBills(username);
        } else {
            System.out.println("Login failed. Exiting...");
        }
    }

    private boolean userExists(String username) {
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

    private int getUserId(String username) {
        String sql = "SELECT id FROM budgeting WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user ID: " + e.getMessage());
        }
        return -1; // Return an invalid user ID if not found
    }

    private void manageBills(String username) {
        int userId = getUserId(username);
        double currentBalance = getBalanceFromDB(userId);
        double totalSavings = SavingManager.getTotalSavings(userId);

        System.out.println("Your current balance: $" + currentBalance);
        System.out.println("Your total savings: $" + totalSavings);

        System.out.println("\nYour savings history:");
        SavingManager.displaySavingHistory(userId);

        System.out.print("\nEnter your total income for this month: $");
        double income = getValidAmount();
        currentBalance += income;
        updateBalance(userId, currentBalance);
        System.out.println("Your updated balance: $" + currentBalance);

        System.out.print("\nDo you want to save money? (yes/no): ");
        String saveChoice = scanner.nextLine();
        if (saveChoice.equalsIgnoreCase("yes")) {
            System.out.print("Enter saving amount: $");
            double saveAmount = getValidAmount();
            if (saveAmount <= currentBalance) {
                SavingManager.addSavings(userId, saveAmount);
                currentBalance -= saveAmount;
                updateBalance(userId, currentBalance);
                System.out.println("Savings of $" + saveAmount + " added successfully!");
            } else {
                System.out.println("Not enough balance to save this amount.");
            }
        }

        System.out.println("\nEnter your bill amounts for this month.");
        double energyBill = getBillAmount("Energy Bill");
        double waterBill = getBillAmount("Water Bill");
        double rentBill = getBillAmount("Rent Bill");

        double totalBills = energyBill + waterBill + rentBill;
        if (currentBalance >= totalBills) {
            currentBalance -= totalBills;
            updateBalance(userId, currentBalance);
            System.out.println("\nBills deducted successfully. Your new balance: $" + currentBalance);
        } else {
            System.out.println("\nNot enough balance to cover the bills.");
        }

        TrackDailySpending.trackSpending(userId, currentBalance);
    }

    private double getValidAmount() {
        while (true) {
            try {
                double amount = Double.parseDouble(scanner.nextLine());
                if (amount < 0) {
                    System.out.println("Amount cannot be negative. Please enter a valid amount.");
                    continue;
                }
                return amount;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private double getBillAmount(String billName) {
        System.out.print("Enter " + billName + " amount: $");
        return getValidAmount();
    }

    private double getBalanceFromDB(int userId) {
        String sql = "SELECT balance FROM budgeting WHERE id = ?";
        double balance = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        } catch (SQLException e) {
            System.out.println("Database error during balance retrieval: " + e.getMessage());
        }
        return balance;
    }

    private void updateBalance(int userId, double newBalance) {
        String sql = "UPDATE budgeting SET balance = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error during balance update: " + e.getMessage());
        }
    }
}
