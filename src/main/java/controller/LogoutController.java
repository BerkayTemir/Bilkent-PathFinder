package controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LogoutController {
    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        Node button = (Node) event.getSource();
        Scene currentScene = button.getScene();
        Stage stage = (Stage) currentScene.getWindow();

        Parent loginView = FXMLLoader.load(getClass().getResource("/LoginView.fxml"));
        stage.setScene(new Scene(loginView, currentScene.getWidth(), currentScene.getHeight()));
        
    }
}
