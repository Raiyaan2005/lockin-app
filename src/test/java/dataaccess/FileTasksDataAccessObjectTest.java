package dataaccess;

import entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileTasksDataAccessObjectTest {

    @TempDir
    Path tempDir;

    private FileTasksDataAccessObject dao;
    private final LocalDate due = LocalDate.of(2025, 12, 1);

    @BeforeEach
    void setUp() {
        File csv = tempDir.resolve("tasks.csv").toFile();
        dao = new FileTasksDataAccessObject(csv.getAbsolutePath());
        dao.setCurrentUsername("alice");
    }

    private Task makeTask(int id, String title) {
        return new Task(id, title, "desc", due, "CSC207");
    }

    @Test
    void testAddAndGetAllTasks() {
        dao.addTask(makeTask(1, "Essay"));
        List<Task> tasks = dao.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals("Essay", tasks.get(0).getTitle());
    }

    @Test
    void testGetAllTasksReturnsEmptyWhenNoneAdded() {
        assertTrue(dao.getAllTasks().isEmpty());
    }

    @Test
    void testGetAllTasksOnlyReturnsCurrentUsersTasks() {
        dao.addTask(makeTask(1, "Alice task"));

        dao.setCurrentUsername("bob");
        dao.addTask(makeTask(2, "Bob task"));

        dao.setCurrentUsername("alice");
        List<Task> aliceTasks = dao.getAllTasks();
        assertEquals(1, aliceTasks.size());
        assertEquals("Alice task", aliceTasks.get(0).getTitle());
    }

    @Test
    void testRemoveTask() {
        Task task = makeTask(42, "To Remove");
        dao.addTask(task);
        dao.removeTask(task);
        assertTrue(dao.getAllTasks().isEmpty());
    }

    @Test
    void testUpdateTask() {
        Task task = makeTask(10, "Original");
        dao.addTask(task);

        task.setTitle("Updated");
        dao.updateTask(task);

        List<Task> tasks = dao.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals("Updated", tasks.get(0).getTitle());
    }

    @Test
    void testDeleteAllTasksForUser() {
        dao.addTask(makeTask(1, "Task 1"));
        dao.addTask(makeTask(2, "Task 2"));
        dao.deleteAllTasksForUser("alice");

        assertTrue(dao.getAllTasks().isEmpty());
    }

    @Test
    void testDeleteAllTasksForUserLeavesOtherUsersData() {
        dao.addTask(makeTask(1, "Alice task"));
        dao.setCurrentUsername("bob");
        dao.addTask(makeTask(2, "Bob task"));

        dao.deleteAllTasksForUser("alice");

        dao.setCurrentUsername("bob");
        assertEquals(1, dao.getAllTasks().size());
    }

    @Test
    void testCompletedFlagPersists() {
        Task task = makeTask(5, "Lab");
        task.setCompleted(true);
        dao.addTask(task);

        Task loaded = dao.getAllTasks().get(0);
        assertTrue(loaded.isCompleted());
    }

    @Test
    void testGetAllTasksReturnsEmptyWhenNoUsernameSet() {
        dao.setCurrentUsername("");
        dao.addTask(makeTask(1, "Orphan"));
        assertTrue(dao.getAllTasks().isEmpty());
    }
}
