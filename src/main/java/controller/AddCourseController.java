package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import java.time.DayOfWeek;
import java.time.LocalTime;
import model.Course;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import java.io.IOException;
import java.time.format.DateTimeParseException;

public class AddCourseController {

    @FXML private TextField courseNameField;
    @FXML private ComboBox<DayOfWeek> dayComboBox;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private TextField locationField;

    @FXML
    public void initialize() {
        dayComboBox.getItems().addAll(DayOfWeek.values());
    }
    

    public Course getCourse() {

        String name = courseNameField.getText();
        DayOfWeek day = dayComboBox.getValue();

        LocalTime startTime =
            LocalTime.parse(startTimeField.getText());

        LocalTime endTime =
            LocalTime.parse(endTimeField.getText());

        String location = locationField.getText();

        if (name.isBlank() || day == null
                || location.isBlank()
                || !endTime.isAfter(startTime)) {
            throw new IllegalArgumentException();
        }

        return new Course(name, day, startTime, endTime, location);
    }
}