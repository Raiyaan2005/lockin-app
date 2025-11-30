package usecase.sync_task;

import entity.Task;
import interfaceadapter.calendar.CalendarController;

import java.awt.*;

public class SyncTaskToCalendarInteractor implements SyncTaskToCalendarInputBoundary {

    private final CalendarController calendarController;
    private final SyncTaskToCalendarOutputBoundary presenter;

    public SyncTaskToCalendarInteractor(CalendarController controller,
                                        SyncTaskToCalendarOutputBoundary presenter) {
        this.calendarController = controller;
        this.presenter = presenter;
    }

    @Override
    public void sync(Task task) {
        String name = task.getTitle();
        java.time.LocalDate date = task.getDate();
        Color color = chooseColor(task.getType());

        calendarController.addEvent(name, date, color);

        presenter.present();
    }

    private Color chooseColor(String type) {
        if (type == null) return Color.GRAY;

        switch (type.toLowerCase()) {
            case "assignment": return Color.BLUE;
            case "test": return Color.RED;
            case "event": return Color.BLACK;
            case "reminder": return Color.ORANGE;
            default: return Color.GRAY;
        }
    }
}
