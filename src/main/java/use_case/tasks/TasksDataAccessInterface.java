package use_case.tasks;

import entity.Task;
import java.util.List;

public interface TasksDataAccessInterface {
    List<Task> getAllTasks();
}
