package entity;

import java.time.LocalDate;

public class Task {

    private final int id;
    private String title;
    private String description;
    private LocalDate date;
    private String course;      // now a Course object
    private boolean completed;

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
    }

    // --- Getters ---
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

    // --- Setters ---
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

    public void markCompleted() {
        this.completed = true;
    }

    public void markIncomplete() {
        this.completed = false;
    }
}