package com.budget.tool.Controller.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;
import com.budget.tool.DatabaseSetup;

public class DashboardController implements Initializable {

    @FXML public Text user_name;
    @FXML public Label login_date, checking_bal, checking_acc_num, saving_bal, saving_acc_num;
    @FXML public Label income_lbl, expenses_lbl;
    @FXML public ListView<String> transaction_listview;
    @FXML public ProgressIndicator budget_bar;

    private final DatabaseSetup db = new DatabaseSetup();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadBalances();
        loadTransactions();
        loadBudgetSummary();
    }

    private void loadBalances() {
        checking_bal.setText("$" + String.format("%.2f", db.getBalance("checking")));
        saving_bal.setText("$" + String.format("%.2f", db.getBalance("savings")));
    }

    private void loadTransactions() {
        transaction_listview.getItems().clear();
        transaction_listview.getItems().addAll(db.getRecentTransactions());
    }

    private void loadBudgetSummary() {
        double income = db.getTotalIncome();
        double expenses = db.getTotalExpenses();
        income_lbl.setText("+ $" + String.format("%.2f", income));
        expenses_lbl.setText("- $" + String.format("%.2f", expenses));
        budget_bar.setProgress(income > 0 ? (income - expenses) / income : 0);
    }
}
