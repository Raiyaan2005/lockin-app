package app;

import javax.swing.*;

import data_access.InMemoryCalendarRepository;
import interface_adapter.calendar.AddEventPresenter;
import interface_adapter.calendar.CalendarController;
import interface_adapter.calendar.CalendarViewModel;
import interface_adapter.calendar.ViewCalendarPresenter;
import use_case.calendar.AddEventInputBoundary;
import use_case.calendar.AddEventInteractor;
import use_case.calendar.AddEventOutputBoundary;
import use_case.calendar.ViewCalendarInputBoundary;
import use_case.calendar.ViewCalendarInteractor;
import use_case.calendar.ViewCalendarOutputBoundary;
import view.CalendarPanel;
import interface_adapter.sync_task.SyncTaskToCalendarController;
import interface_adapter.sync_task.SyncTaskToCalendarPresenter;
import use_case.sync_task.SyncTaskToCalendarInputBoundary;
import use_case.sync_task.SyncTaskToCalendarInteractor;
import use_case.sync_task.SyncTaskToCalendarOutputBoundary;
import view.TasksPanel;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            InMemoryCalendarRepository repo = new InMemoryCalendarRepository();
            CalendarViewModel calendarViewModel = new CalendarViewModel(repo);

            AddEventOutputBoundary addPresenter = new AddEventPresenter(calendarViewModel);
            AddEventInputBoundary addInteractor = new AddEventInteractor(repo, addPresenter);

            ViewCalendarOutputBoundary viewPresenter = new ViewCalendarPresenter(calendarViewModel);
            ViewCalendarInputBoundary viewInteractor = new ViewCalendarInteractor(viewPresenter);

            CalendarController calendarController =
                    new CalendarController(addInteractor, viewInteractor);

            CalendarPanel.sharedViewModel = calendarViewModel;
            CalendarPanel.sharedCalendarController = calendarController;

            SyncTaskToCalendarOutputBoundary syncPresenter = new SyncTaskToCalendarPresenter();
            SyncTaskToCalendarInputBoundary syncInteractor =
                    new SyncTaskToCalendarInteractor(calendarController, syncPresenter);
            SyncTaskToCalendarController syncController =
                    new SyncTaskToCalendarController(syncInteractor);

            TasksPanel.syncController = syncController;



            AppBuilder appBuilder = new AppBuilder();
            JFrame application = appBuilder
                    .addLoginView()
                    .addSignupView()
                    .addLoggedInView()
                    .addSignupUseCase()
                    .addLoginUseCase()
                    .addChangePasswordUseCase()
                    .addLogoutUseCase()
                    .build();

            application.pack();
            application.setLocationRelativeTo(null);
            application.setVisible(true);
        });
    }
}
