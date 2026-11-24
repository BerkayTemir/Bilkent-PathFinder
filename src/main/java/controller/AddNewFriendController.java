package controller;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import service.FriendRequestService;

public class AddNewFriendController {

    @FXML private TextField usernameField;
    @FXML private Button sendButton;
    @FXML private Label messageLabel;

    private Parent friendsView;

    public void setFriendsView(Parent friendsView) {
        this.friendsView = friendsView;
    }

    @FXML
    private void sendRequest() {
        String username = usernameField.getText().strip();

        if (username.startsWith("@")) {
            username = username.substring(1).strip();
        }

        if (username.isEmpty()) {
            showMessage("Enter a username.", false);
            return;
        }

        Scene scene = sendButton.getScene();
        Object sessionService = scene.getProperties().get(FriendRequestService.class);

        if (!(sessionService instanceof FriendRequestService service)) {
            showMessage("Sign in before sending a friend request.", false);
            return;
        }

        sendButton.setDisable(true);
        showMessage("Sending request...", true);

        service.send(username).whenComplete((result, error) -> Platform.runLater(() -> {
            sendButton.setDisable(false);

            if (sendButton.getScene() != scene) {
                return;
            }

            if (error != null) {
                showMessage("Could not send the request. Try again.", false);
                return;
            }

            switch (result) {
                case SENT -> {
                    usernameField.clear();
                    showMessage("Friend request sent.", true);
                }
                case NOT_FOUND -> showMessage("User not found.", false);
                case SELF -> showMessage("You cannot add yourself.", false);
                case EXISTS -> showMessage("A request or friendship already exists.", false);
                case SIGN_IN -> showMessage("Sign in again to send a request.", false);
                case INVALID_USERNAME -> showMessage("Enter a valid username.", false);
            }
        }));
    }

    @FXML
    private void backToFriends() {
        sendButton.getScene().setRoot(friendsView);
    }

    @FXML
    private void showNavigation() throws IOException {
        Parent mainView = FXMLLoader.load(getClass().getResource("/MainView.fxml"));
        sendButton.getScene().setRoot(mainView);
    }

    private void showMessage(String text, boolean positive) {
        messageLabel.setText(text);
        messageLabel.setStyle(positive ? "-fx-text-fill: #166534;" : "-fx-text-fill: #B91C1C;");
        messageLabel.setManaged(true);
        messageLabel.setVisible(true);
    }
}
