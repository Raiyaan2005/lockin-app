package interfaceadapter.task;

import usecase.task.AddTaskInputBoundary;
import usecase.task.AddTaskInputData;

import java.time.LocalDate;

public class TaskController {

    private final AddTaskInputBoundary interactor;

    public TaskController(AddTaskInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void addTask(String title, String description, LocalDate date, String type, String course) {
        AddTaskInputData input = new AddTaskInputData(title, description, date, type, course);
        interactor.add(input);
    }
}
