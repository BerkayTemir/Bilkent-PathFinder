package controller;

import java.sql.SQLException;

import Database.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import service.AuthService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML 
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML 
    private TextField surnameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleSignup() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String name = nameField.getText();
        String surname = surnameField.getText();
        String email = emailField.getText().trim();

        if (username.isBlank() || email.isBlank() || password.isBlank() || name.isBlank() || surname.isBlank()){
            messageLabel.setText("Please fill in all fields!");
            return;
        }

       if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match!");
            return;
        }
        
        try {
            AuthService authService = new AuthService();

            int status = authService.signUp(email, password, username, name, surname);

            if (status == 200 || status == 201) {
                messageLabel.setText("Account created successfully!");
            } else {
                messageLabel.setText("Registration failed! Status: " + status);
            }

        } catch (Exception e) {
            messageLabel.setText("Connection error!");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGoToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/LoginView.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Bilkent Pathfinder - Login");

        } catch (Exception e) {
            messageLabel.setText("Could not open Login.");
            e.printStackTrace();
        }
    }
}
