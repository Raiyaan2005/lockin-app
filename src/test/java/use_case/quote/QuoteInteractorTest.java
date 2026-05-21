package use_case.quote;

import entity.Quote;
import org.junit.jupiter.api.Test;
import usecase.quote.*;

import static org.junit.jupiter.api.Assertions.*;

class QuoteInteractorTest {

    private static class PresenterSpy implements QuoteOutputBoundary {
        QuoteOutputData presented;
        String error;

        @Override
        public void presentQuote(QuoteOutputData data) { presented = data; }

        @Override
        public void presentError(String message) { error = message; }
    }

    @Test
    void testSuccessPassesQuoteToPresenter() {
        QuoteGateway gateway = () -> new Quote("Be yourself.", "Socrates");
        PresenterSpy spy = new PresenterSpy();
        new QuoteInteractor(gateway, spy).loadQuote();

        assertNotNull(spy.presented);
        assertEquals("Be yourself.", spy.presented.getContent());
        assertEquals("Socrates", spy.presented.getAuthor());
        assertNull(spy.error);
    }

    @Test
    void testGatewayExceptionPresentsErrorMessage() {
        QuoteGateway gateway = () -> { throw new RuntimeException("network error"); };
        PresenterSpy spy = new PresenterSpy();
        new QuoteInteractor(gateway, spy).loadQuote();

        assertNull(spy.presented);
        assertEquals("Could not load quote.", spy.error);
    }
}
