package interfaceadapter.quote;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class QuoteViewModel {

    public static final String QUOTE_TEXT = "quoteText";

    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    private String quoteHtml = "";

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public String getQuoteHtml() {
        return quoteHtml;
    }

    public void setQuoteHtml(String newQuoteHtml) {
        String old = this.quoteHtml;
        this.quoteHtml = newQuoteHtml;
        support.firePropertyChange(QUOTE_TEXT, old, newQuoteHtml);
    }
}
