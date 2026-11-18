package controller;

import java.sql.SQLException;

import Database.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import security.PasswordHasher;

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML 
    private TextField nameField;

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

        if (username.isBlank() || password.isBlank() || name.isBlank() || surname.isBlank()){
            messageLabel.setText("Please fill in all fields!");
            return;
        }

        if (password.equals(confirmPassword)) {
            messageLabel.setText("Passwords match!");
        }
        else {
            messageLabel.setText("Passwords do not match!");
            return;
        }
        
        String hashedPassword = PasswordHasher.hashPassword(password);

        try {
            UserDAO userDAO = new UserDAO();

            boolean success = userDAO.createUser(username, name, surname, hashedPassword);

        if (success){
            messageLabel.setText("Account created successfully!");
        }

        }catch (SQLException e){

            if (e.getSQLState().equals("23505")) {
                messageLabel.setText("Username already exists!");
            } else {
                /* messageLabel.setText("Registration failed!");
                e.printStackTrace(); */
                    messageLabel.setText("Registration failed!");
    System.out.println("SQL State: " + e.getSQLState());
    System.out.println("Error: " + e.getMessage());
    e.printStackTrace();
            }
        }
    }

    
}
