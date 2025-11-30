package interface_adapter.tasks;

import use_case.tasks.TasksInputBoundary;
import use_case.tasks.TasksInputData;

public class TasksController {

    private final TasksInputBoundary interactor;

    public TasksController(TasksInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadTasks() {
        interactor.execute(new TasksInputData());
    }
}
