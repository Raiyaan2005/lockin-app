package dataaccess;

import entity.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCalendarRepositoryTest {

    private InMemoryCalendarRepository repo;
    private final LocalDate today = LocalDate.of(2025, 6, 1);

    @BeforeEach
    void setUp() {
        repo = new InMemoryCalendarRepository();
    }

    @Test
    void testAddAndRetrieveEvent() {
        Event event = new Event("Lecture", today, Color.BLUE);
        repo.add(event);

        List<Event> events = repo.eventsOn(today);
        assertEquals(1, events.size());
        assertEquals("Lecture", events.get(0).getName());
    }

    @Test
    void testEventsOnDateWithNoEventsReturnsEmptyList() {
        assertTrue(repo.eventsOn(today).isEmpty());
    }

    @Test
    void testEventsAreIsolatedByDate() {
        repo.add(new Event("A", today, Color.RED));
        repo.add(new Event("B", today.plusDays(1), Color.GREEN));

        assertEquals(1, repo.eventsOn(today).size());
        assertEquals(1, repo.eventsOn(today.plusDays(1)).size());
    }

    @Test
    void testClearRemovesAllEvents() {
        repo.add(new Event("A", today, Color.RED));
        repo.add(new Event("B", today.plusDays(5), Color.GREEN));
        repo.clear();

        assertTrue(repo.eventsOn(today).isEmpty());
        assertTrue(repo.eventsOn(today.plusDays(5)).isEmpty());
    }

    @Test
    void testMarkCompletedSetsFlag() {
        repo.add(new Event("Assignment", today, Color.BLUE));
        repo.markCompleted("Assignment", today);

        assertTrue(repo.eventsOn(today).get(0).isCompleted());
    }

    @Test
    void testMarkCompletedOnlyAffectsMatchingEvent() {
        repo.add(new Event("A", today, Color.BLUE));
        repo.add(new Event("B", today, Color.RED));
        repo.markCompleted("A", today);

        List<Event> events = repo.eventsOn(today);
        boolean aCompleted = events.stream().filter(e -> e.getName().equals("A")).findFirst().orElseThrow().isCompleted();
        boolean bCompleted = events.stream().filter(e -> e.getName().equals("B")).findFirst().orElseThrow().isCompleted();

        assertTrue(aCompleted);
        assertFalse(bCompleted);
    }
}
