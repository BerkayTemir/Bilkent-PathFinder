package model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Course {

    private String name;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;

    public Course(String name, DayOfWeek day, LocalTime startTime, LocalTime endTime, String location) {

        this.name = name;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return name + " | " + day + " | "  + startTime + " - " + endTime  + " | " + location;
    }
}