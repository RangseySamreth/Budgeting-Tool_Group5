package com.budget.tool.Controller.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import com.budget.tool.DatabaseSetup;

public class AccountsController implements Initializable {

    @FXML public Label ch_acc_num, transaction_limit, ch_acc_date, ch_acc_bal;
    @FXML public Label sav_acc_num, withdrawal_limit, sav_acc_date, sav_acc_bal;
    @FXML public TextField amount_to_sav, amount_to_ch;
    @FXML public Button trans_to_sav_btn, trans_to_ch_btn;

    private final DatabaseSetup db = new DatabaseSetup();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateBalances();
        trans_to_sav_btn.setOnAction(event -> transferFunds("checking", "savings"));
        trans_to_ch_btn.setOnAction(event -> transferFunds("savings", "checking"));
    }

    private void updateBalances() {
        ch_acc_bal.setText("$" + String.format("%.2f", db.getBalance("checking")));
        sav_acc_bal.setText("$" + String.format("%.2f", db.getBalance("savings")));
    }

    private void transferFunds(String from, String to) {
        try {
            double amount = (from.equals("checking"))
                    ? Double.parseDouble(amount_to_sav.getText())
                    : Double.parseDouble(amount_to_ch.getText());

            db.transfer(from, to, amount);
            updateBalances();

            if (from.equals("checking")) amount_to_sav.clear();
            else amount_to_ch.clear();
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        }
    }
}
