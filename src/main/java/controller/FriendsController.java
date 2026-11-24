package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class FriendsController {

    @FXML private Button navigationButton;
    @FXML private Button favoritePlacesButton;
    @FXML private Button scheduleButton;
    @FXML private Button friendsButton;
    @FXML private Button addFriendButton;


    @FXML
    private void showNavigation() throws IOException {
        System.out.println("Navigation tiklandi");
        Parent mainView = FXMLLoader.load(
            getClass().getResource("/MainView.fxml")
        );

        navigationButton.getScene().setRoot(mainView);
    } 

    @FXML
    private void showAddFriend(ActionEvent event) throws IOException {
        var scene = ((Node) event.getSource()).getScene();

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/AddNewFriend.fxml")
        );
        Parent addFriendView = loader.load();

        AddNewFriendController controller = loader.getController();
        controller.setFriendsView(scene.getRoot());

        scene.setRoot(addFriendView);
    }
}