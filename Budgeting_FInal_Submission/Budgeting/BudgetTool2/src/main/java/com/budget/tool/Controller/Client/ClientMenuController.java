package com.budget.tool.Controller.Client;

import com.budget.tool.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    public Button dashboard_btn;
    public Button transaction_btn;
    public Button accounts_btn;
    public Button budget_btn;
    public Button bills_btn;
    public Button profile_btn;
    public Button logout_btn;
    public Button report_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();

    }
    private void addListeners (){
   /*     dashboard_btn.setOnAction(actionEvent -> onDashboard());
        transaction_btn.setOnAction(actionEvent -> onTransactions());
        accounts_btn.setOnAction(actionEvent -> onAccounts());*/
        budget_btn.setOnAction(actionEvent -> onBudget());
        bills_btn.setOnAction(actionEvent -> onPayBills());
    }

  /*  private void onDashboard() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set("Dashboard");
    }

    private void onTransactions() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set("Transactions");
    }

    private void onAccounts() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set("Accounts");
    }
*/
    private void onBudget() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set("Budget");
    }

    private void onPayBills(){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set("PayBills");
    }
}
