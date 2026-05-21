package interfaceadapter.login;

import interfaceadapter.ViewManagerModel;
import interfaceadapter.logged_in.LoggedInViewModel;
import interfaceadapter.tasks.TasksViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecase.login.LoginOutputData;
import usecase.tasks.TasksDataAccessInterface;
import entity.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoginPresenterTest {

    private static class StubTasksDao implements TasksDataAccessInterface {
        String lastUsername;

        @Override
        public void setCurrentUsername(String username) { lastUsername = username; }

        @Override
        public List<Task> getAllTasks() { return List.of(); }

        @Override
        public void addTask(Task task) {}

        @Override
        public void updateTask(Task task) {}

        @Override
        public void removeTask(Task task) {}
    }

    private ViewManagerModel viewManagerModel;
    private LoggedInViewModel loggedInViewModel;
    private LoginViewModel loginViewModel;
    private StubTasksDao tasksDao;
    private LoginPresenter presenter;

    @BeforeEach
    void setUp() {
        viewManagerModel = new ViewManagerModel();
        loggedInViewModel = new LoggedInViewModel();
        loginViewModel = new LoginViewModel();
        tasksDao = new StubTasksDao();
        presenter = new LoginPresenter(viewManagerModel, loggedInViewModel, loginViewModel, tasksDao);
    }

    @Test
    void testPrepareSuccessViewSetsUsernameAndSwitchesView() {
        presenter.prepareSuccessView(new LoginOutputData("alice"));

        assertEquals("alice", tasksDao.lastUsername, "DAO should track the logged-in user.");
        assertEquals("alice", loggedInViewModel.getState().getUsername(), "LoggedIn state should reflect username.");
        assertEquals(loggedInViewModel.getViewName(), viewManagerModel.getState(), "View manager should switch to logged-in view.");
        assertNull(loginViewModel.getState().getLoginError(), "Login error should be cleared on success.");
    }

    @Test
    void testPrepareFailViewSetsErrorOnLoginState() {
        presenter.prepareFailView("Invalid credentials.");

        assertEquals("Invalid credentials.", loginViewModel.getState().getLoginError());
        assertEquals("", viewManagerModel.getState(), "View should not switch on failure.");
    }
}
