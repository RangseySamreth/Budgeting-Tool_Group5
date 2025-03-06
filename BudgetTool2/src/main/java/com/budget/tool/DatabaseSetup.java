package com.budget.tool;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
    private static final String DB_PATH = "budgeting.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    public static void main(String[] args) {
        deleteOldDatabase();
        createDatabase();
        checkDatabasePath();
    }

    public static void deleteOldDatabase() {
        File dbFile = new File(DB_PATH);
        if (dbFile.exists()) {
            if (dbFile.delete()) {
                System.out.println("Old database deleted successfully.");
            } else {
                System.out.println("Failed to delete old database.");
            }
        } else {
            System.out.println("No existing database found.");
        }
    }

    public static void createDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                System.out.println("Connected to SQLite database.");
                createTables(conn);
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    public static void createTables(Connection conn) {
        String createBudgetingTable = "CREATE TABLE IF NOT EXISTS budgeting ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT UNIQUE NOT NULL, "
                + "password TEXT NOT NULL, "
                + "balance REAL DEFAULT 0"
                + ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createBudgetingTable);
            System.out.println("Budgeting table created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    public static void checkDatabasePath() {
        File dbFile = new File(DB_PATH);
        System.out.println("Database file path: " + dbFile.getAbsolutePath());
    }
}