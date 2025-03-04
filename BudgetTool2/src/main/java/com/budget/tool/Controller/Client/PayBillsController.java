package com.budget.tool.Controller.Client;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class PayBillsController implements Initializable {
    private static final String DB_URL = "jdbc:sqlite:budgeting0.db";
    private int userId = 1;
    private double currentBalance;

    public TextField incomeField;
    public Button income_btn;
    public TextField energyBillField;
    public TextField waterBillField;
    public TextField rentBillField;
    public Button payBills_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentBalance = getBalanceFromDB(userId);
        income_btn.setOnAction(this::updateIncome);
        payBills_btn.setOnAction(this::payBills);
    }

    private void updateIncome(ActionEvent event) {
        try {
            double income = Double.parseDouble(incomeField.getText());
            if (income < 0) {
                showAlert("Invalid Input", "Income cannot be negative.");
                return;
            }
            currentBalance += income;
            updateBalance(userId, currentBalance);
            showAlert("Income Updated", "Your new balance: $" + currentBalance);
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid income amount.");
        }
    }

    private void payBills(ActionEvent event) {
        try {
            double energyBill = Double.parseDouble(energyBillField.getText());
            double waterBill = Double.parseDouble(waterBillField.getText());
            double rentBill = Double.parseDouble(rentBillField.getText());

            double totalBills = energyBill + waterBill + rentBill;
            if (totalBills > currentBalance) {
                showAlert("Insufficient Funds", "Not enough balance to cover bills.");
                return;
            }

            currentBalance -= totalBills;
            updateBalance(userId, currentBalance);
            showAlert("Bills Paid", "Total bills: $" + totalBills + "\nRemaining Balance: $" + currentBalance);
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numeric bill amounts.");
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

    private void updateBalance(int userId, double newBalance) {
        String sql = "UPDATE budgeting SET balance = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            showAlert("Database Error", "Error updating balance: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
