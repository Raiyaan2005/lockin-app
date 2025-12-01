package dataaccess;

import entity.Quote;
import usecase.quote.QuoteGateway;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class QuoteApiClient implements QuoteGateway {

    // Inspirational / success / education quotes, pipes URL-encoded
    private static final String QUOTE_URL =
            "https://api.quotable.io/random?tags=inspirational%7Csuccess%7Ceducation";

    private final HttpClient client;

    public QuoteApiClient() {
        this.client = createInsecureClient();
    }

    /** Trust-all SSL client (fine for this course project, not for production). */
    private HttpClient createInsecureClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) { }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) { }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            return HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create insecure HttpClient", e);
        }
    }

    @Override
    public Quote fetchRandomQuote() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(QUOTE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            String body = response.body();

            String content = extractField(body, "content");
            String author = extractField(body, "author");

            return new Quote(content, author);

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch quote", e);
        }
    }

    // Tiny helper: looks for "field":"value"
    private String extractField(String json, String field) {
        String key = "\"" + field + "\":\"";
        int start = json.indexOf(key);
        if (start == -1) return "";
        start += key.length();
        int end = json.indexOf('"', start);
        if (end == -1) return "";
        return json.substring(start, end);
    }
}
