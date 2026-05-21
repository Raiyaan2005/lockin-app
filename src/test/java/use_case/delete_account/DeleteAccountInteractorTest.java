package use_case.delete_account;

import entity.Task;
import org.junit.jupiter.api.Test;
import usecase.delete_account.*;
import usecase.tasks.TasksDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteAccountInteractorTest {

    private static class MockUserDao implements DeleteAccountUserDataAccessInterface {
        private String currentUsername = "alice";
        final List<String> deleted = new ArrayList<>();

        @Override
        public String getCurrentUsername() { return currentUsername; }

        @Override
        public void deleteUser(String username) { deleted.add(username); }

        @Override
        public void setCurrentUsername(String username) { currentUsername = username; }
    }

    private static class MockTasksDao implements TasksDataAccessInterface {
        final List<String> deletedForUser = new ArrayList<>();

        @Override
        public List<Task> getAllTasks() { return List.of(); }

        @Override
        public void addTask(Task task) {}

        @Override
        public void updateTask(Task task) {}

        @Override
        public void removeTask(Task task) {}

        @Override
        public void deleteAllTasksForUser(String username) { deletedForUser.add(username); }
    }

    private static class PresenterSpy implements DeleteAccountOutputBoundary {
        boolean called;

        @Override
        public void prepareSuccessView() { called = true; }
    }

    @Test
    void testDeleteRemovesUserAndTasksAndClearsSession() {
        MockUserDao userDao = new MockUserDao();
        MockTasksDao tasksDao = new MockTasksDao();
        PresenterSpy spy = new PresenterSpy();

        new DeleteAccountInteractor(userDao, tasksDao, spy).execute();

        assertTrue(spy.called, "Presenter should be notified of success.");
        assertEquals(List.of("alice"), tasksDao.deletedForUser, "Tasks for the user must be deleted.");
        assertEquals(List.of("alice"), userDao.deleted, "User account must be deleted.");
        assertNull(userDao.getCurrentUsername(), "Session must be cleared after deletion.");
    }
}
