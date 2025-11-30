package usecase.task;

import java.time.LocalDate;

public class AddTaskInputData {
    private final String title;
    private final String description;
    private final LocalDate date;
    private final String type;
    private final String course;

    public AddTaskInputData(String title,
                            String description,
                            LocalDate date,
                            String type,
                            String course) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.type = type;
        this.course = course;
    }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public LocalDate getDate() { return date; }

    public String getType() { return type; }

    public String getCourse() { return course; }
}
