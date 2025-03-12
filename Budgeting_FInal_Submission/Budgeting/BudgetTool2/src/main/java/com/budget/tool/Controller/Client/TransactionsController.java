/*package com.budget.tool.Controller.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.ResourceBundle;
import com.budget.tool.DatabaseSetup;

public class TransactionsController implements Initializable {

    @FXML public ListView<String> transaction_listview;
    private final DatabaseSetup db = new DatabaseSetup();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTransactions();
    }

    private void loadTransactions() {
        transaction_listview.getItems().clear();
        transaction_listview.getItems().addAll(db.getRecentTransactions());
    }
}*/
