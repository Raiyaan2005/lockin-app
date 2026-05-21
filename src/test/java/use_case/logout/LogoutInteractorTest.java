package use_case.logout;

import org.junit.jupiter.api.Test;
import usecase.logout.*;

import static org.junit.jupiter.api.Assertions.*;

class LogoutInteractorTest {

    private static class MockDao implements LogoutUserDataAccessInterface {
        private String currentUsername = "alice";

        @Override
        public String getCurrentUsername() { return currentUsername; }

        @Override
        public void setCurrentUsername(String username) { currentUsername = username; }
    }

    private static class PresenterSpy implements LogoutOutputBoundary {
        LogoutOutputData received;

        @Override
        public void prepareSuccessView(LogoutOutputData data) { received = data; }
    }

    @Test
    void testLogoutClearsSessionAndPassesUsernameToPresenter() {
        MockDao dao = new MockDao();
        PresenterSpy spy = new PresenterSpy();
        new LogoutInteractor(dao, spy).execute();

        assertNull(dao.getCurrentUsername(), "Current username should be null after logout.");
        assertNotNull(spy.received, "Presenter should have been called.");
        assertEquals("alice", spy.received.getUsername(), "Output data should carry the logged-out username.");
    }
}
