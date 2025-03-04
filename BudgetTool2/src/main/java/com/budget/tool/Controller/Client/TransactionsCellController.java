package com.budget.tool.Controller.Client;

import com.budget.tool.Models.Transaction;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.kordamp.ikonli.javafx.FontIcon;
import java.net.URL;
import java.util.ResourceBundle;

public class TransactionsCellController implements Initializable {

    public FontIcon in_icon, out_icon;
    public Label tran_date_lbl, tran_user_lbl, expense_lbl, amount_lbl;
    private Transaction transaction;

    public TransactionsCellController() {}

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        //tran_date_lbl.setText(transaction.getDate());
       // tran_user_lbl.setText(transaction.getUser());
        expense_lbl.setText(transaction.getCategory());
        amount_lbl.setText("$" + String.format("%.2f", transaction.getAmount()));

        if (transaction.getAmount() < 0) {
            out_icon.setVisible(true);
            in_icon.setVisible(false);
        } else {
            out_icon.setVisible(false);
            in_icon.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}
