package interfaceadapter.signup;

import interfaceadapter.ViewManagerModel;
import interfaceadapter.login.LoginViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecase.signup.SignupOutputData;

import static org.junit.jupiter.api.Assertions.*;

class SignupPresenterTest {

    private ViewManagerModel viewManagerModel;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private SignupPresenter presenter;

    @BeforeEach
    void setUp() {
        viewManagerModel = new ViewManagerModel();
        signupViewModel = new SignupViewModel();
        loginViewModel = new LoginViewModel();
        presenter = new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);
    }

    @Test
    void testPrepareSuccessViewSwitchesToLoginAndSetsUsername() {
        presenter.prepareSuccessView(new SignupOutputData("alice"));

        assertEquals("alice", loginViewModel.getState().getUsername(), "Login state should pre-fill the newly created username.");
        assertEquals(loginViewModel.getViewName(), viewManagerModel.getState(), "View should switch to the login screen.");
    }

    @Test
    void testPrepareFailViewSetsUsernameError() {
        presenter.prepareFailView("User already exists.");

        assertEquals("User already exists.", signupViewModel.getState().getUsernameError());
        assertEquals("", viewManagerModel.getState(), "View should not switch on failure.");
    }

    @Test
    void testSwitchToLoginViewChangesActiveView() {
        presenter.switchToLoginView();

        assertEquals(loginViewModel.getViewName(), viewManagerModel.getState());
    }
}
