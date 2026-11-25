package controller;

import java.io.IOException;
import java.time.format.DateTimeParseException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import model.Course;

import javafx.collections.ObservableList; // to add schedule to memory
import javafx.collections.FXCollections;

import java.time.LocalDate; //available for checking if a class is today
import java.time.LocalTime;
import java.time.DayOfWeek;
import javafx.scene.paint.Color;

public class ScheduleController extends MainPageController {
    private static ObservableList<Course> courses =
    FXCollections.observableArrayList();

    @FXML private Label statusLabel;
    @FXML private Label nextClassLabel;
    @FXML private Label remainingTimeLabel;

    @FXML private ListView<Course> courseListView;

    @FXML private Button addCourseButton;
    @FXML private Button removeCourseButton;

    @FXML
    public void initialize() {
        statusLabel.setText("Available");
        nextClassLabel.setText("No upcoming class");
        remainingTimeLabel.setText("-");

        courseListView.setItems(courses); // connect the list view to the observable list of courses
        updateStatus(); // to check if the user is available or not when the page is loaded
    }
    @FXML
    private void addCourse() throws IOException {

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/AddCourseView.fxml")
        );

        Parent content = loader.load();

        AddCourseController controller = loader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Add Course");
        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane().getButtonTypes().addAll(
            ButtonType.OK, ButtonType.CANCEL
        );

        if (dialog.showAndWait().orElse(ButtonType.CANCEL)
                == ButtonType.OK) {

            try {
                Course course = controller.getCourse();
                courses.add(course);
                updateStatus();
            }
            catch (DateTimeParseException | IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter valid course information.");
                alert.showAndWait();
            }
        }
    }
    @FXML
    private void removeCourse() {

        Course selectedCourse =
            courseListView.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            courses.remove(selectedCourse);
            updateStatus();
        }
    }
    private void updateStatus() {

    DayOfWeek today = LocalDate.now().getDayOfWeek();
    LocalTime now = LocalTime.now();

    boolean available = true;

    for (int i = 0; i < courses.size(); i++) {

        Course course = courses.get(i);

        if (course.getDay() == today
                && !now.isBefore(course.getStartTime())
                && now.isBefore(course.getEndTime())) {

            available = false;
        }
    }

    if (available) {
        statusLabel.setText("Available");
        statusLabel.setTextFill(Color.GREEN);
    }
    else {
        statusLabel.setText("Unavailable");
        statusLabel.setTextFill(Color.RED);
    }
}
}