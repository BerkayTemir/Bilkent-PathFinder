package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class MainPageController{

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

        var scene = friendsButton.getScene();
        scene.setRoot(friendsView);
        ((Stage) scene.getWindow()).setTitle("Bilkent Pathfinder - Friends");
    }
    @FXML
    protected void showSchedule() throws IOException {
        System.out.println("showSchedule called");
        Parent scheduleView = FXMLLoader.load(
            getClass().getResource("/ScheduleView.fxml")
        );

        var scene = scheduleButton.getScene();
        scene.setRoot(scheduleView);
        ((Stage) scene.getWindow()).setTitle("Bilkent Pathfinder - Schedule");
    }

    @FXML
    private void showNavigation() throws IOException {
        Parent mainView = FXMLLoader.load(
            getClass().getResource("/MainView.fxml")
        );

        var scene = navigationButton.getScene();
        scene.setRoot(mainView);
        ((Stage) scene.getWindow()).setTitle("Bilkent Pathfinder - Navigation");
    } 
}
