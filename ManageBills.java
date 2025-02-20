package com.budget.tool;

import java.sql.*;
import java.util.Scanner;

public class ManageBills {

    private static final String DB_URL = "jdbc:sqlite:budgeting.db";

    public static void manageBills(int userId) {
        Scanner scanner = new Scanner(System.in);
        try {
            double balance = getBalanceFromDB(userId);
            System.out.println("Your current balance: $" + balance);

            System.out.print("\nEnter your total income for this month: $");
            double income = getUserAmount(scanner);
            balance += income;
            updateBalance(userId, balance);
            System.out.println("Your updated balance: $" + balance);

            System.out.print("\nDo you want to save some money? (yes/no): ");
            String saveChoice = scanner.nextLine();
            if (saveChoice.equalsIgnoreCase("yes")) {
                System.out.print("Enter the amount you want to save: $");
                double saveAmount = getUserAmount(scanner);
                SavingManager.addSavings(userId, saveAmount);  // Corrected method call
                System.out.println("Your savings have been updated.");
            }

            System.out.println("\nEnter your bill amounts for this month.");
            double energyBill = getUserAmount(scanner, "Energy Bill");
            double waterBill = getUserAmount(scanner, "Water Bill");
            double rentBill = getUserAmount(scanner, "Rent Bill");

            double totalBills = energyBill + waterBill + rentBill;
            if (balance >= totalBills) {
                balance -= totalBills;
                updateBalance(userId, balance);
                System.out.println("\nBills deducted successfully.");
                System.out.println("Your new balance: $" + balance);
            } else {
                System.out.println("\nNot enough balance to cover the bills.");
            }

            // Call to track daily spending
            TrackDailySpending.trackSpending(userId, balance); // Call to TrackDailySpending after all transactions
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static double getUserAmount(Scanner scanner, String billType) {
        double amount = -1;
        while (amount < 0) {
            try {
                System.out.print("Enter your " + billType + ": $");
                amount = Double.parseDouble(scanner.nextLine());
                if (amount < 0) {
                    System.out.println("Please enter a positive amount for " + billType + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for " + billType + ". Please enter a valid number.");
            }
        }
        return amount;
    }

    private static double getUserAmount(Scanner scanner) {
        double amount = -1;
        while (amount < 0) {
            try {
                amount = Double.parseDouble(scanner.nextLine());
                if (amount < 0) {
                    System.out.println("Please enter a positive amount.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return amount;
    }

    private static void updateBalance(int userId, double newBalance) throws SQLException {
        String sql = "UPDATE budgeting SET balance = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setDouble(1, newBalance);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    private static double getBalanceFromDB(int userId) throws SQLException {
        String sql = "SELECT balance FROM budgeting WHERE id = ?";
        double balance = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        }
        return balance;
    }
}
