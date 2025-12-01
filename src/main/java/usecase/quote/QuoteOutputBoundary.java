package usecase.quote;

public interface QuoteOutputBoundary {
    void presentQuote(QuoteOutputData data);
    void presentError(String message);
}
