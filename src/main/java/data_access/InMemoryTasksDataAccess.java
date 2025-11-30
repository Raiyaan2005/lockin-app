package data_access;

import entity.Task;
import use_case.tasks.TasksDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTasksDataAccess implements TasksDataAccessInterface {

    private final List<Task> tasks = new ArrayList<>();

    @Override
    public List<Task> getAllTasks() {
        // This is what the Interactor will use
        return new ArrayList<>(tasks);
    }

    // Extra helpers for the UI (TasksPanel):

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public void updateTask(Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == updatedTask.getId()) {
                tasks.set(i, updatedTask);
                return;
            }
        }
    }
}
