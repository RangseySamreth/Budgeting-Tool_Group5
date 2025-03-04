package com.budget.tool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSetup {
    private static final String DB_PATH = "budgeting.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    public static void main(String[] args) {
        System.out.println("Initializing database setup...");
        createDatabase();
        System.out.println("Database setup complete.");
    }

    private static void createDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                System.out.println("Connected to SQLite database.");
                createTables(conn);
                seedData(conn);  // Add initial data
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    // Create required tables
    private static void createTables(Connection conn) {
        String createAccountsTable = "CREATE TABLE IF NOT EXISTS accounts ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "type TEXT UNIQUE NOT NULL, "
                + "balance REAL DEFAULT 0"
                + ");";

        String createTransactionsTable = "CREATE TABLE IF NOT EXISTS transactions ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "date TEXT NOT NULL, "
                + "category TEXT NOT NULL, "
                + "amount REAL NOT NULL"
                + ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createAccountsTable);
            stmt.execute(createTransactionsTable);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    // Seed Initial Data (Checking & Savings accounts)
    private static void seedData(Connection conn) {
        String insertChecking = "INSERT OR IGNORE INTO accounts (type, balance) VALUES ('checking', 1000.0)";
        String insertSavings = "INSERT OR IGNORE INTO accounts (type, balance) VALUES ('savings', 5000.0)";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(insertChecking);
            stmt.execute(insertSavings);
            System.out.println("Initial account data added.");
        } catch (SQLException e) {
            System.out.println("Error seeding database: " + e.getMessage());
        }
    }

    // Get Balance from Accounts Table
    public double getBalance(String accountType) {
        String query = "SELECT balance FROM accounts WHERE type = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, accountType);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getDouble("balance") : 0.0;
        } catch (SQLException e) {
            System.out.println("Error fetching balance: " + e.getMessage());
            return 0.0;
        }
    }

    // Transfer Funds Between Accounts
    public void transfer(String from, String to, double amount) {
        String deductQuery = "UPDATE accounts SET balance = balance - ? WHERE type = ?";
        String addQuery = "UPDATE accounts SET balance = balance + ? WHERE type = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);
            try (PreparedStatement deductStmt = conn.prepareStatement(deductQuery);
                 PreparedStatement addStmt = conn.prepareStatement(addQuery)) {

                deductStmt.setDouble(1, amount);
                deductStmt.setString(2, from);
                deductStmt.executeUpdate();

                addStmt.setDouble(1, amount);
                addStmt.setString(2, to);
                addStmt.executeUpdate();

                conn.commit();
                System.out.println("Transferred $" + amount + " from " + from + " to " + to);
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Transfer failed: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    //  Get Recent Transactions
    public List<String> getRecentTransactions() {
        List<String> transactions = new ArrayList<>();
        String query = "SELECT date, category, amount FROM transactions ORDER BY date DESC LIMIT 10";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                transactions.add(rs.getString("date") + " - " + rs.getString("category") + ": $" + rs.getDouble("amount"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
        }
        return transactions;
    }

    // Get Total Income
    public double getTotalIncome() {
        String query = "SELECT SUM(amount) FROM transactions WHERE amount > 0"; // Positive amounts = Income
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        } catch (SQLException e) {
            System.out.println("Error fetching total income: " + e.getMessage());
            return 0.0;
        }
    }

    //  Get Total Expenses (Bills + Spending)
    public double getTotalExpenses() {
        String query = "SELECT SUM(amount) FROM transactions WHERE amount < 0"; // Negative amounts = Expenses
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? Math.abs(rs.getDouble(1)) : 0.0; // Convert to positive value
        } catch (SQLException e) {
            System.out.println("Error fetching total expenses: " + e.getMessage());
            return 0.0;
        }
    }
}
