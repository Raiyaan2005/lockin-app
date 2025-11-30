package interfaceadapter.task;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AddTaskViewModel {

    // Property name constants (UI listens for these)
    public static final String ADD_TASK_SUCCESS = "addTaskSuccess";
    public static final String ADD_TASK_FAILURE = "addTaskFailure";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    // Fields holding state for the UI
    private String errorMessage = "";
    private boolean lastActionSuccessful = false;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /** Called by presenter if the task was added successfully */
    public void fireSuccess() {
        this.lastActionSuccessful = true;
        support.firePropertyChange(ADD_TASK_SUCCESS, null, null);
    }

    /** Called by presenter if the use case failed (validation error etc.) */
    public void fireFailure(String message) {
        this.lastActionSuccessful = false;
        this.errorMessage = message;
        support.firePropertyChange(ADD_TASK_FAILURE, null, null);
    }

    public boolean wasLastActionSuccessful() {
        return lastActionSuccessful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
