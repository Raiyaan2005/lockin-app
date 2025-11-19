package use_case.sync_task;

import entity.Task;

public interface SyncTaskToCalendarInputBoundary {
    void sync(Task task);
}
