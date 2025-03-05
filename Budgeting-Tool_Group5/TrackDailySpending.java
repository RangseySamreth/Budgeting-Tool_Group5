package com.budget.tool;

import java.sql.*;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class TrackDailySpending {
    private static final String DB_URL = "jdbc:sqlite:budgeting.db";
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, Double> spendingCategories = new HashMap<>();

    // Method to track daily spending and update the balance
    public static void trackSpending(int userId, double currentBalance) {
        double dailyLimit;
        double totalSpending = 0;
        boolean overspent = false;

        // Ensure the daily spending limit does not exceed the monthly budget
        while (true) {
            System.out.print("Enter how much you would like to spend each day: $");
            dailyLimit = getUserAmount();

            if (dailyLimit * 31 > currentBalance) {
                System.out.println("Warning: Your daily spending limit will exceed your available balance in a month!");
                System.out.println("Please enter a lower daily limit.");
            } else {
                break;
            }
        }

        System.out.println("Your daily limit is set to: $" + dailyLimit);

        // Proceed to enter categories for spending
        totalSpending = trackCategories(totalSpending, currentBalance);

        // Check if total spending exceeds the daily limit
        if (totalSpending > dailyLimit) {
            System.out.println("Warning: Your total spending exceeds your daily limit!");
            overspent = true;
        }

        // Final balance update after spending
        if (totalSpending <= currentBalance) {
            double newBalance = currentBalance - totalSpending;
            updateBalance(userId, newBalance);

            // Print receipt
            printReceipt(totalSpending, overspent, currentBalance, newBalance);
        } else {
            System.out.println("Your spending exceeds your available balance. Transaction cancelled.");
        }
    }

    private static double getUserAmount() {
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

    // Method to track different spending categories
    private static double trackCategories(double totalSpending, double currentBalance) {
        boolean addingCategories = true;

        while (addingCategories) {
            System.out.print("Enter spending category name (e.g., Food, Drink, Shopping): ");
            String category = scanner.nextLine();
            System.out.print("Enter " + category + " spending: $");
            double amount = getUserAmount();

            spendingCategories.put(category, amount);  // Store the category and its spending
            totalSpending += amount;
            System.out.println("You spent $" + amount + " on " + category);

            // If the total spending exceeds the balance
            if (totalSpending > currentBalance) {
                System.out.println("Warning: Your spending has exceeded the current balance.");
                break;  // Exit if the spending exceeds the balance
            }

            System.out.print("Would you like to add another category? (yes/no): ");
            String addMore = scanner.nextLine();
            if (addMore.equalsIgnoreCase("no")) {
                addingCategories = false;
            }
        }

        return totalSpending;
    }

    // Method to update the balance in the database after spending
    private static void updateBalance(int userId, double newBalance) {
        String sql = "UPDATE budgeting SET balance = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            System.out.println("Your balance has been updated.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Method to print the receipt and show remaining balance
    private static void printReceipt(double totalSpending, boolean overspent, double oldBalance, double newBalance) {
        System.out.println("\n----- Today's Receipt -----");
        System.out.println("Total spending for today: $" + totalSpending);

        if (overspent) {
            System.out.println("Warning: You have exceeded your daily limit!");
        } else {
            System.out.println("You are within your budget.");
        }

        System.out.println("Your old balance: $" + oldBalance);
        System.out.println("Your new balance: $" + newBalance);
        System.out.println("----- End of Receipt -----\n");
    }
}
