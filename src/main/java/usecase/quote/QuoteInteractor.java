package usecase.quote;

import entity.Quote;

public class QuoteInteractor implements QuoteInputBoundary {

    private final QuoteGateway gateway;
    private final QuoteOutputBoundary presenter;

    public QuoteInteractor(QuoteGateway gateway,
                           QuoteOutputBoundary presenter) {
        this.gateway = gateway;
        this.presenter = presenter;
    }

    @Override
    public void loadQuote() {
        try {
            Quote quote = gateway.fetchRandomQuote();
            QuoteOutputData outputData =
                    new QuoteOutputData(quote.getContent(), quote.getAuthor());
            presenter.presentQuote(outputData);
        } catch (RuntimeException e) {
            presenter.presentError("Could not load quote.");
        }
    }
}
