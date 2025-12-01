package interfaceadapter.quote;

import usecase.quote.QuoteOutputBoundary;
import usecase.quote.QuoteOutputData;

public class QuotePresenter implements QuoteOutputBoundary {

    private final QuoteViewModel viewModel;

    public QuotePresenter(QuoteViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentQuote(QuoteOutputData data) {
        String html = "<html><div style='text-align:right;'>" +
                "<i>\"" + data.getContent() + "\"</i><br/>— " + data.getAuthor() +
                "</div></html>";
        viewModel.setQuoteHtml(html);
    }

    @Override
    public void presentError(String message) {
        viewModel.setQuoteHtml(message);
    }
}
