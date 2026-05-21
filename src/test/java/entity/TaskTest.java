package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private final LocalDate due = LocalDate.of(2025, 12, 1);

    @Test
    void testDefaultsAfterConstruction() {
        Task task = new Task(1, "Essay", "Write it", due, "ENG101");
        assertEquals(1, task.getId());
        assertEquals("Essay", task.getTitle());
        assertEquals("Write it", task.getDescription());
        assertEquals(due, task.getDate());
        assertEquals("ENG101", task.getCourse());
        assertFalse(task.isCompleted(), "Tasks should not be completed by default.");
        assertEquals("Task", task.getType(), "Default type should be 'Task'.");
    }

    @Test
    void testSetters() {
        Task task = new Task(2, "Old", "Old desc", due, "OLD");
        task.setTitle("New");
        task.setDescription("New desc");
        task.setDate(due.plusDays(7));
        task.setCourse("CSC207");
        task.setCompleted(true);
        task.setType("Assignment");

        assertEquals("New", task.getTitle());
        assertEquals("New desc", task.getDescription());
        assertEquals(due.plusDays(7), task.getDate());
        assertEquals("CSC207", task.getCourse());
        assertTrue(task.isCompleted());
        assertEquals("Assignment", task.getType());
    }

    @Test
    void testNullDateIsAllowed() {
        Task task = new Task(3, "Title", "Desc", null, "MAT");
        assertNull(task.getDate());
    }
}
