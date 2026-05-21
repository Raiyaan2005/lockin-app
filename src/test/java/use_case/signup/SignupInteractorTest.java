package use_case.signup;

import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;
import usecase.signup.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SignupInteractorTest {

    private static class MockDao implements SignupUserDataAccessInterface {
        private final Map<String, User> store = new HashMap<>();

        @Override
        public boolean existsByName(String username) { return store.containsKey(username); }

        @Override
        public void save(User user) { store.put(user.getName(), user); }

        boolean contains(String username) { return store.containsKey(username); }
    }

    private static class PresenterSpy implements SignupOutputBoundary {
        boolean successCalled;
        boolean failCalled;
        boolean switchCalled;
        String failMessage;
        String successUsername;

        @Override
        public void prepareSuccessView(SignupOutputData data) {
            successCalled = true;
            successUsername = data.getUsername();
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failCalled = true;
            failMessage = errorMessage;
        }

        @Override
        public void switchToLoginView() { switchCalled = true; }
    }

    private SignupInteractor make(MockDao dao, PresenterSpy spy) {
        return new SignupInteractor(dao, spy, new UserFactory());
    }

    @Test
    void testSuccessCreatesAndSavesUser() {
        MockDao dao = new MockDao();
        PresenterSpy spy = new PresenterSpy();
        make(dao, spy).execute(new SignupInputData("alice", "pass123", "pass123"));

        assertTrue(spy.successCalled);
        assertFalse(spy.failCalled);
        assertEquals("alice", spy.successUsername);
        assertTrue(dao.contains("alice"));
    }

    @Test
    void testUserAlreadyExists() {
        MockDao dao = new MockDao();
        dao.save(new UserFactory().create("alice", "hashed"));
        PresenterSpy spy = new PresenterSpy();
        make(dao, spy).execute(new SignupInputData("alice", "pass", "pass"));

        assertTrue(spy.failCalled);
        assertEquals("User already exists.", spy.failMessage);
    }

    @Test
    void testPasswordMismatch() {
        PresenterSpy spy = new PresenterSpy();
        make(new MockDao(), spy).execute(new SignupInputData("bob", "abc", "xyz"));

        assertTrue(spy.failCalled);
        assertEquals("Passwords don't match.", spy.failMessage);
    }

    @Test
    void testEmptyPassword() {
        PresenterSpy spy = new PresenterSpy();
        make(new MockDao(), spy).execute(new SignupInputData("bob", "", ""));

        assertTrue(spy.failCalled);
        assertEquals("New password cannot be empty", spy.failMessage);
    }

    @Test
    void testEmptyUsername() {
        PresenterSpy spy = new PresenterSpy();
        make(new MockDao(), spy).execute(new SignupInputData("", "pass", "pass"));

        assertTrue(spy.failCalled);
        assertEquals("Username cannot be empty", spy.failMessage);
    }

    @Test
    void testSwitchToLoginView() {
        PresenterSpy spy = new PresenterSpy();
        make(new MockDao(), spy).switchToLoginView();

        assertTrue(spy.switchCalled);
    }
}
