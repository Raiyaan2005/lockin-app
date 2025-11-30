package interfaceadapter.dashboard;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import entity.Task;
import interfaceadapter.tasks.dto.TaskDTO;

public class DashboardViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private List<TaskDTO> dueSoonTaskDTOs;

    /**
     * Sets the list of TaskDTOs due soon and notifies listeners.
     */
    public void setDueSoonTasks(List<TaskDTO> taskDTOs) {
        List<TaskDTO> oldDTOs = this.dueSoonTaskDTOs;
        this.dueSoonTaskDTOs = taskDTOs;
        support.firePropertyChange("dueSoonTasks", oldDTOs, taskDTOs);
    }

    /**
     * Returns the current list of TaskDTOs due soon.
     */
    public List<TaskDTO> getDueSoonTasks() {
        return dueSoonTaskDTOs;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**
     * Update top 3 due soon tasks from the domain Task list and map to DTOs.
     */
    public void updateDueSoonTasks(List<Task> allTasks) {
        List<Task> sorted = allTasks.stream()
                .filter(t -> !t.isCompleted() && t.getDate() != null)
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .toList();

        List<Task> top3 = sorted.size() > 3 ? sorted.subList(0, 3) : sorted;

        List<TaskDTO> dtoList = mapTasksToDTOs(top3);

        setDueSoonTasks(dtoList);
    }

    /**
     * Maps domain Task entities to TaskDTOs for the UI.
     */
    private List<TaskDTO> mapTasksToDTOs(List<Task> tasks) {
        return tasks.stream()
                .map(t -> new TaskDTO(
                        t.getTitle(),
                        t.getCourse(),
                        t.getDescription(),
                        t.getDate(),
                        t.isCompleted()
                ))
                .toList();
    }
}
