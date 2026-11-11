package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML
    private StackPane mapContainer;

    @FXML
    private void handleNavigation() {
        System.out.println("Navigation clicked!");
    }
}