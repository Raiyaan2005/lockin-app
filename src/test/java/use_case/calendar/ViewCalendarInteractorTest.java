package use_case.calendar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewCalendarInteractorTest {

    @Test
    void testViewCalendarCallsPresenter() {
        TestPresenter presenter = new TestPresenter();
        ViewCalendarInteractor interactor = new ViewCalendarInteractor(presenter);

        interactor.viewCalendar(new ViewCalendarInputData());

        assertTrue(presenter.called, "Presenter should have been called.");
        assertNotNull(presenter.output, "Output data should not be null.");
    }

    private static class TestPresenter implements ViewCalendarOutputBoundary {
        boolean called = false;
        ViewCalendarOutputData output;

        @Override
        public void prepareSuccessView(ViewCalendarOutputData data) {
            this.called = true;
            this.output = data;
        }
    }
}
