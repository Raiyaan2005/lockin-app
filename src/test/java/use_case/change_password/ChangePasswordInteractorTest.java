package use_case.change_password;

import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import usecase.change_password.*;

import static org.junit.jupiter.api.Assertions.*;

class ChangePasswordInteractorTest {

    private static class MockDao implements ChangePasswordUserDataAccessInterface {
        User saved;

        @Override
        public void changePassword(User user) { saved = user; }
    }

    private static class PresenterSpy implements ChangePasswordOutputBoundary {
        boolean successCalled;
        boolean failCalled;
        String failMessage;
        String successUsername;

        @Override
        public void prepareSuccessView(ChangePasswordOutputData data) {
            successCalled = true;
            successUsername = data.getUsername();
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failCalled = true;
            failMessage = errorMessage;
        }
    }

    @Test
    void testSuccessHashesAndSavesNewPassword() {
        MockDao dao = new MockDao();
        PresenterSpy spy = new PresenterSpy();
        new ChangePasswordInteractor(dao, spy, new UserFactory())
                .execute(new ChangePasswordInputData("newSecret", "alice"));

        assertTrue(spy.successCalled);
        assertFalse(spy.failCalled);
        assertEquals("alice", spy.successUsername);
        assertNotNull(dao.saved);
        assertTrue(BCrypt.checkpw("newSecret", dao.saved.getPassword()),
                "Stored password should be a bcrypt hash of the new password.");
    }

    @Test
    void testEmptyPasswordFails() {
        PresenterSpy spy = new PresenterSpy();
        new ChangePasswordInteractor(new MockDao(), spy, new UserFactory())
                .execute(new ChangePasswordInputData("", "alice"));

        assertTrue(spy.failCalled);
        assertEquals("New password cannot be empty", spy.failMessage);
        assertFalse(spy.successCalled);
    }
}
