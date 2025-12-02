package entity;

import java.time.LocalDate;

public class Task {

    private final int id;
    private String title;
    private String description;
    private LocalDate date;
    private String course;
    private boolean completed;
    private String type;

    public Task(int id,
                String title,
                String description,
                LocalDate date,
                String course) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.course = course;
        this.completed = false;
        this.type = "Task";
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCourse() {
        return course;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getType() {
        return type;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
