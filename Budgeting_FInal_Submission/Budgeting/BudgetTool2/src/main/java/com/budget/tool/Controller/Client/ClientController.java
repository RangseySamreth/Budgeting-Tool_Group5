package com.budget.tool.Controller.Client;

import com.budget.tool.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    public BorderPane client_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
         switch (newVal){
          /*   case "Transactions" -> client_parent.setCenter(Model.getInstance().getViewFactory().getTransactionsView());
             case "Accounts" -> client_parent.setCenter(Model.getInstance().getViewFactory().getAccountsView());
             case "Budget" -> client_parent.setCenter(Model.getInstance().getViewFactory().getBudgetView());*/
             case "PayBills" -> client_parent.setCenter(Model.getInstance().getViewFactory().getPaybillsView());
             default -> client_parent.setCenter(Model.getInstance().getViewFactory().getBudgetView());
         //    default -> client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
         }
        });

    }
}
