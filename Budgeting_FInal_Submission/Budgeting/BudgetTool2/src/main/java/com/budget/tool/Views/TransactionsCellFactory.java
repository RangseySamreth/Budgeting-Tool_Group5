/*package com.budget.tool.Views;

import com.budget.tool.Controller.Client.TransactionsCellController;
import com.budget.tool.Models.Transaction;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.Parent;

public class TransactionsCellFactory extends ListCell<Transaction> {
    @Override
    protected void updateItem(Transaction transaction, boolean empty) {
        super.updateItem(transaction, empty);
        if (empty || transaction == null) {
            setText(null);
            setGraphic(null);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/TransactionsCell.fxml"));
                Parent root = loader.load();

                // Get the controller and set the transaction data
                TransactionsCellController controller = loader.getController();
                controller.setTransaction(transaction);

                setGraphic(root);
                setText(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}*/
