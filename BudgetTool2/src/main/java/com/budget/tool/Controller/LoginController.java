package com.budget.tool.Controller;

import com.budget.tool.Models.Model;
import com.budget.tool.LoginUser;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox<String> acc_selector;
    public Label payee_address_lbl;
    public TextField payee_address_fld;
    public PasswordField password_fld;
    public Button login_btn;
    public Label error_lbl;

    private final LoginUser loginUser;

    public LoginController() {
        this.loginUser = new LoginUser(); // Initialize login handler
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acc_selector.getItems().addAll("Existing User", "New User");
        acc_selector.setValue("Existing User");

        acc_selector.setOnAction(event -> updateLoginButton());

        login_btn.setOnAction(actionEvent -> handleAuth());
    }

    private void updateLoginButton() {
        if (acc_selector.getValue().equals("New User")) {
            login_btn.setText("Sign Up");
        } else {
            login_btn.setText("Login");
        }
    }

    private void handleAuth() {
        String username = payee_address_fld.getText();
        String password = password_fld.getText();

        if (username.isEmpty() || password.isEmpty()) {
            error_lbl.setText("Please fill in all fields.");
            return;
        }

        if (acc_selector.getValue().equals("New User")) {
            if (loginUser.userExists(username)) {
                error_lbl.setText("User already exists.");
            } else {
                loginUser.registerUser(username, password);
                error_lbl.setText("Sign-up complete! Switch to 'Existing User' to log in.");
            }
        } else {
            if (loginUser.login(username, password)) {
                closeLoginScreen();
                Model.getInstance().getViewFactory().showClientWindow();
            } else {
                error_lbl.setText("Invalid login credentials.");
            }
        }
    }

    private void closeLoginScreen() {
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }
}
