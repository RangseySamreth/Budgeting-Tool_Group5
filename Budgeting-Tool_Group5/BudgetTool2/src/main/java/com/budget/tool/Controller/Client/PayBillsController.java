package com.budget.tool.Controller.Client;

import com.budget.tool.SessionManager;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class PayBillsController implements Initializable {
    private static final String DB_URL = "jdbc:sqlite:budgeting.db";
    private int userId;
    private double currentBalance;

    public TextField incomeField;
    public Button income_btn;
    public TextField energyBillField;
    public TextField waterBillField;
    public TextField rentBillField;
    public Button payBills_btn;
    public Label balanceLabel; // Label to display the balance

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userId = getLoggedInUserId();
        if (userId == -1) {
            showAlert("Error", "User not found. Please log in again.");
            return;
        }
        loadUserBalance();

        income_btn.setOnAction(this::updateIncome);
        payBills_btn.setOnAction(this::payBills);
    }

    private int getLoggedInUserId() {
        // Retrieve the user ID from session storage
        return SessionManager.getCurrentUserId();
    }

    private void loadUserBalance() {
        currentBalance = getBalanceFromDB(userId);
        updateBalanceUI();
    }

    private void updateIncome(ActionEvent event) {
        try {
            if (incomeField.getText().isEmpty()) {
                showAlert("Invalid Input", "Income field cannot be empty.");
                return;
            }

            double income = Double.parseDouble(incomeField.getText());
            if (income <= 0) {
                showAlert("Invalid Input", "Income must be a positive number.");
                return;
            }

            updateBalance(userId, income);
            showAlert("Income Updated", "Your new balance: $" + currentBalance);
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid numeric income amount.");
        }
    }

    private void payBills(ActionEvent event) {
        try {
            double energyBill = getValidBillAmount(energyBillField, "Energy Bill");
            double waterBill = getValidBillAmount(waterBillField, "Water Bill");
            double rentBill = getValidBillAmount(rentBillField, "Rent Bill");

            double totalBills = energyBill + waterBill + rentBill;
            if (totalBills > currentBalance) {
                showAlert("Insufficient Funds", "Not enough balance to cover bills.");
                return;
            }

            updateBalance(userId, -totalBills);
            showAlert("Bills Paid", "Total bills: $" + totalBills + "\nRemaining Balance: $" + currentBalance);
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numeric bill amounts.");
        }
    }

    private double getValidBillAmount(TextField billField, String billName) {
        try {
            if (billField.getText().isEmpty()) {
                showAlert("Invalid Input", billName + " field cannot be empty.");
                throw new NumberFormatException("Empty input");
            }

            double amount = Double.parseDouble(billField.getText());
            if (amount < 0) {
                showAlert("Invalid Input", billName + " amount cannot be negative.");
                throw new NumberFormatException("Negative amount");
            }
            return amount;
        } catch (NumberFormatException e) {
            throw e; // Re-throw to be caught in payBills()
        }
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
            showAlert("Database Error", "Error fetching balance: " + e.getMessage());
        }
        return balance;
    }

    private void updateBalance(int userId, double amountChange) {
        double newBalance = currentBalance + amountChange;
        if (newBalance < 0) {
            showAlert("Error", "Insufficient funds.");
            return;
        }

        String sql = "UPDATE budgeting SET balance = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            currentBalance = newBalance;
            updateBalanceUI();
        } catch (SQLException e) {
            showAlert("Database Error", "Error updating balance: " + e.getMessage());
        }
    }

    private void updateBalanceUI() {
        if (balanceLabel != null) {
            balanceLabel.setText("Balance: $" + currentBalance);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
