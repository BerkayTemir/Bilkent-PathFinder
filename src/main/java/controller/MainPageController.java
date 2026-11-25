package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class MainPageController {

    @FXML private Button navigationButton;
    @FXML private Button favoritePlacesButton;
    @FXML private Button scheduleButton;
    @FXML private Button friendsButton;

    @FXML private StackPane mapContainer;

    @FXML
    private void showFriends() throws IOException {
        Parent friendsView = FXMLLoader.load(
            getClass().getResource("/FriendsPageView.fxml")
        );

        friendsButton.getScene().setRoot(friendsView);
    }
    @FXML
    protected void showSchedule() throws IOException {
        Parent scheduleView = FXMLLoader.load(
            getClass().getResource("/ScheduleView.fxml")
        );

        scheduleButton.getScene().setRoot(scheduleView);
    }

    @FXML
    private void showNavigation() throws IOException {
        Parent mainView = FXMLLoader.load(
            getClass().getResource("/MainView.fxml")
        );

        navigationButton.getScene().setRoot(mainView);
    } 
}
