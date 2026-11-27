package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import model.Friend;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import service.FriendRequestService;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class FriendsController {

    @FXML private Button navigationButton;
    @FXML private Button favoritePlacesButton;
    @FXML private Button scheduleButton;
    @FXML private Button friendsButton;
    @FXML private Button addFriendButton;

    private final List<Friend> friends = new ArrayList<>();

        @FXML private VBox incomingRequestsList;
        @FXML private VBox friendsList;

       @FXML
        private void initialize() {
            incomingRequestsList.sceneProperty().addListener((observable, oldScene, newScene) -> {
                if (newScene != null) {
                    loadIncomingRequests();
                }
            });
        }

        private void loadIncomingRequests() {
            Scene scene = incomingRequestsList.getScene();
            if (scene == null) {
                return;
            }

            Object sessionService = scene.getProperties().get(FriendRequestService.class);
            if (!(sessionService instanceof FriendRequestService requestService)) {
                incomingRequestsList.getChildren().setAll(
                        new Label("Friend requests are not available yet."));
                return;
            }
            loadFriends(requestService);
            requestService.listIncomingRequests().whenComplete((requests, error) ->
                    Platform.runLater(() -> {
                        var rows = incomingRequestsList.getChildren();
                        rows.clear();

                        if (error != null) {
                            rows.add(new Label("Could not load requests."));
                            return;
                        }

                        if (requests.isEmpty()) {
                            rows.add(new Label("No incoming requests."));
                            return;
                        }

                        for (var request : requests) {
                            rows.add(createRequestRow(request, requestService));
                        }
                    }));
    }
    private void loadFriends(FriendRequestService requestService) {
        requestService.listFriends().whenComplete((usernames, error) ->
                Platform.runLater(() -> {
                    var rows = friendsList.getChildren();
                    rows.clear();

                    if (error != null) {
                        rows.add(new Label("Could not load friends."));
                        return;
                    }

                    if (usernames.isEmpty()) {
                        rows.add(new Label("No friends yet."));
                        return;
                    }

                    for (String username : usernames) {
                        Label name = new Label(username);
                        name.setStyle("-fx-font-weight: bold; -fx-text-fill: #172B4D;");

                        HBox row = new HBox(name);
                        row.setStyle("-fx-background-color: #F1F5F9;"
                                + "-fx-background-radius: 8; -fx-padding: 8;");
                        rows.add(row);
                    }
                }));
    }

    private HBox createRequestRow(FriendRequestService.IncomingRequest request ,  FriendRequestService requestService) {
        Label username = new Label(request.username());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button accept = new Button("Accept");
        accept.setStyle("-fx-background-color: #3B6FC4; -fx-text-fill: white;"
                + "-fx-background-radius: 6; -fx-padding: 7 12;");

        Button delete = new Button("Delete");
        HBox row = new HBox(8, username, spacer, accept , delete);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: #F1F5F9;"
                + "-fx-background-radius: 8; -fx-padding: 8;");

        accept.setOnAction(event -> {
            accept.setDisable(true);
            delete.setDisable(true);
            accept.setText("Accepting…");

            requestService.acceptRequest(request.id()).whenComplete((accepted, error) ->
                    Platform.runLater(() -> {
                        if (error != null) {
                            accept.setText("Retry");
                            accept.setDisable(false);
                            delete.setDisable(false);
                            return;
                        }
                        loadIncomingRequests();
                    }));
        });
        delete.setOnAction(event -> {
        delete.setDisable(true);
        accept.setDisable(true);

        requestService.deleteRequest(request.id()).whenComplete((deleted, error) ->
                Platform.runLater(() -> {
                    if (error != null) {
                        delete.setText("Retry");
                        delete.setDisable(false);
                        accept.setDisable(false);
                        return;
                    }

                    loadIncomingRequests();
                }));
        });

        return row;
    }

    @FXML
    private void showNavigation() throws IOException {
        System.out.println("Navigation tiklandi");
        Parent mainView = FXMLLoader.load(
            getClass().getResource("/MainView.fxml")
        );

        var scene = navigationButton.getScene();
        scene.setRoot(mainView);
        ((Stage) scene.getWindow()).setTitle("Bilkent Pathfinder - Navigation");
    } 

     @FXML
    protected void showSchedule() throws IOException {
        Parent scheduleView = FXMLLoader.load(
            getClass().getResource("/ScheduleView.fxml")
        );

        var scene = scheduleButton.getScene();
        scene.setRoot(scheduleView);
        ((Stage) scene.getWindow()).setTitle("Bilkent Pathfinder - Schedule");
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