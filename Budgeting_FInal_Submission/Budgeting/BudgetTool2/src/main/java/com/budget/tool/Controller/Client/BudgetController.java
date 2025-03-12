package com.budget.tool.Controller.Client;

import com.budget.tool.ControllerManager;
import com.budget.tool.SessionManager;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class BudgetController implements Initializable {
    private static final String DB_URL = "jdbc:sqlite:budgeting.db";
    private int userId;
    private double currentBalance = 0;
    private double dailyLimit = 0;

    public TextField spendingLimitField;
    public Button set_limit_btn;
    public TextField spendingAmountField;
    public ComboBox<String> categoryChoiceBox1;
    public Button track_spending_btn;
    public ListView<String> spendingListView;
    public ProgressIndicator budgetProgressIndicator;
    public Label remainingBudgetLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userId = SessionManager.getCurrentUserId();
        if (userId == -1) {
            showAlert("Error", "No user session found. Please log in again.");
            return;
        }

        // Register this controller in ControllerManager
        ControllerManager.setBudgetController(this);

        loadUserBalance();
        categoryChoiceBox1.getItems().addAll("Food", "Transport", "Entertainment", "Shopping", "Other");

        set_limit_btn.setOnAction(this::setDailyLimit);
        track_spending_btn.setOnAction(this::trackSpending);
    }

    public void refreshBalance() {
        loadUserBalance(); // Reload balance when bills are paid
    }

    private void loadUserBalance() {
        String sql = "SELECT balance FROM budgeting WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                currentBalance = rs.getDouble("balance");
                updateUI();
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Could not load balance: " + e.getMessage());
        }
    }

    private void setDailyLimit(ActionEvent event) {
        try {
            double limit = Double.parseDouble(spendingLimitField.getText());
            if (limit <= 0) {
                showAlert("Invalid Input", "Daily limit must be a positive number.");
                return;
            }
            if (limit * 31 > currentBalance) {
                showAlert("Warning", "Your daily limit exceeds your monthly budget!");
                return;
            }
            dailyLimit = limit;
            showAlert("Success", "Daily limit set to: $" + dailyLimit);
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number.");
        }
    }

    private void trackSpending(ActionEvent event) {
        if (categoryChoiceBox1.getValue() == null) {
            showAlert("Error", "Please select a category.");
            return;
        }

        try {
            double amount = Double.parseDouble(spendingAmountField.getText());
            if (amount <= 0) {
                showAlert("Invalid Input", "Spending amount must be a positive number.");
                return;
            }
            if (amount > currentBalance) {
                showAlert("Error", "Insufficient funds.");
                return;
            }
            if (amount > dailyLimit) {
                showAlert("Warning", "You have exceeded your daily limit!");
            }

            updateBalance(amount);
            spendingListView.getItems().add(categoryChoiceBox1.getValue() + ": $" + amount);
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid spending amount.");
        }
    }

    private void updateBalance(double spentAmount) {
        double newBalance = currentBalance - spentAmount;
        String sql = "UPDATE budgeting SET balance = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            currentBalance = newBalance;
            updateUI();
        } catch (SQLException e) {
            showAlert("Database Error", "Error updating balance: " + e.getMessage());
        }
    }

    private void updateUI() {
        if (remainingBudgetLabel != null) {
            remainingBudgetLabel.setText("Remaining Balance: $" + currentBalance);
        }
        if (budgetProgressIndicator != null) {
            budgetProgressIndicator.setProgress(currentBalance > 0 ? currentBalance / 1000 : 0);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
