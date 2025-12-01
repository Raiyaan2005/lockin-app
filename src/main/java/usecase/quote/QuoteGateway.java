package usecase.quote;

import entity.Quote;

public interface QuoteGateway {
    Quote fetchRandomQuote();
}
