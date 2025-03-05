package com.budget.tool.Controller.Client;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Text user_name;
    public Label login_date;
    public Label checking_bal;
    public Label checking_acc_num;
    public Label saving_bal;
    public Label saving_acc_num;
    public Label income_lbl;
    public Label expenses_lbl;
    public ListView transaction_listview;
    public ProgressIndicator budget_bar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
