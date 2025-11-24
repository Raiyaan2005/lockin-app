package interface_adapter.sync_task;

import entity.Task;
import use_case.sync_task.SyncTaskToCalendarInputBoundary;

public class SyncTaskToCalendarController {

    private final SyncTaskToCalendarInputBoundary syncInteractor;

    public SyncTaskToCalendarController(SyncTaskToCalendarInputBoundary interactor) {
        this.syncInteractor = interactor;
    }

    public void sync(Task task) {
        syncInteractor.sync(task);
    }
}
