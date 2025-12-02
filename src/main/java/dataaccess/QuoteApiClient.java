package dataaccess;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import entity.Quote;
import usecase.quote.QuoteGateway;

/**
 * Gateway that calls the public Quotable REST API to retrieve a random quote.
 * <p>
 * It requests quotes tagged as inspirational, success, or education.
 */
public class QuoteApiClient implements QuoteGateway {

    /**
     * URL for fetching a random inspirational / success / education quote.
     * The pipe characters between tags are URL-encoded as {@code %7C}.
     */
    private static final String QUOTE_URL =
            "https://api.quotable.io/random?tags=inspirational%7Csuccess%7Ceducation";

    private final HttpClient client;

    /**
     * Constructs a {@code QuoteApiClient} using an HttpClient with a
     * permissive SSL context. This avoids local certificate problems that
     * sometimes appear on student machines. (Not suitable for production.)
     */
    public QuoteApiClient() {
        this.client = createInsecureClient();
    }

    /**
     * Builds an {@link HttpClient} that accepts all SSL certificates.
     * If anything goes wrong while configuring SSL, falls back to the
     * default client.
     *
     * @return an {@link HttpClient} instance
     */
    private HttpClient createInsecureClient() {
        HttpClient result;

        try {
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                            // Trust all client certificates (course project only).
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                            // Trust all server certificates (course project only).
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    },
            };

            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            result = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException sslException) {
            // If custom SSL fails, use the default client instead.
            result = HttpClient.newHttpClient();
        }

        return result;
    }

    /**
     * Fetches a random quote from the Quotable API.
     *
     * @return a {@link Quote} built from the API response, or a fallback quote
     *         if the API request fails for any reason
     */
    @Override
    public Quote fetchRandomQuote() {
        Quote result = new Quote("Could not load quote from the server.", "LockIn");

        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(QUOTE_URL))
                    .GET()
                    .build();

            final HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            final String body = response.body();
            final String content = extractField(body, "content");
            final String author = extractField(body, "author");

            if (!content.isEmpty()) {
                result = new Quote(content, author);
            }
        } catch (IOException ioException) {
            // Network / SSL / other IO issue: keep fallback quote.
        } catch (InterruptedException interruptedException) {
            // Preserve interrupted status and keep fallback quote.
            Thread.currentThread().interrupt();
        }

        return result;
    }

    /**
     * Extracts a string field from the JSON response of the Quotable API.
     * <p>
     * This is a very small helper that looks for a pattern like
     * {@code "field":"value"} in the raw JSON string.
     *
     * @param json  the raw JSON response as a string
     * @param field the name of the field to extract (for example, {@code "content"})
     * @return the value of the field if found; the empty string otherwise
     */
    private String extractField(String json, String field) {
        final String key = "\"" + field + "\":\"";
        final int keyIndex = json.indexOf(key);

        String result = "";

        if (keyIndex != -1) {
            final int valueStart = keyIndex + key.length();
            final int valueEnd = json.indexOf('"', valueStart);
            if (valueEnd != -1) {
                result = json.substring(valueStart, valueEnd);
            }
        }

        return result;
    }
}
