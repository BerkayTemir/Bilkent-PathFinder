package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.AuthService;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleLogin() {

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        AuthService authService = new AuthService();

        try {
            if (!authService.login(username, password)) {
                messageLabel.setText("Invalid username or password!");
                return;
            }

            messageLabel.setText("Login successful!");

            Parent homePage = FXMLLoader.load(
                getClass().getResource("/MainView.fxml")
            );

            messageLabel.getScene().setRoot(homePage);

        } catch (Exception e) {
            messageLabel.setText("An error occurred!");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleForgotPassword() {

        // TODO: Open a screen that asks for the user's email.
        messageLabel.setText("Please enter your email to reset your password.");
    }

    @FXML
    private void handleCreateAccount() {

        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/SignUpView.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) usernameField
                .getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Bilkent Pathfinder - Sign Up");

        } catch (Exception e) {
            messageLabel.setText("Could not open Sign Up.");
            e.printStackTrace();
        }
    }
    
}
