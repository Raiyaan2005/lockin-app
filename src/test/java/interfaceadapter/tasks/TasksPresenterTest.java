package interfaceadapter.tasks;

import entity.Task;
import org.junit.jupiter.api.Test;
import usecase.tasks.TasksOutputData;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TasksPresenterTest {

    @Test
    void testPrepareSuccessViewPopulatesViewModel() {
        TasksViewModel viewModel = new TasksViewModel();
        TasksPresenter presenter = new TasksPresenter(viewModel);

        List<Task> tasks = List.of(new Task(1, "Essay", "Write it", LocalDate.now(), "ENG101"));
        presenter.prepareSuccessView(new TasksOutputData(tasks));

        assertEquals(tasks, viewModel.getTasks(), "ViewModel should hold the tasks from output data.");
    }

    @Test
    void testPrepareSuccessViewWithEmptyList() {
        TasksViewModel viewModel = new TasksViewModel();
        TasksPresenter presenter = new TasksPresenter(viewModel);

        presenter.prepareSuccessView(new TasksOutputData(List.of()));

        assertNotNull(viewModel.getTasks());
        assertTrue(viewModel.getTasks().isEmpty());
    }
}
