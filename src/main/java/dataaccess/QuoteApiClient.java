package dataaccess;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.util.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import entity.Quote;
import entity.Task;
import usecase.task.TaskDataAccessInterface;

public class QuoteApiClient {

    // Only quotes tagged with inspirational, success, or education
    private static final String QUOTE_URL =
            "https://api.quotable.io/random?tags=inspirational%7Csuccess%7Ceducation";

    private final HttpClient client;

    public QuoteApiClient() {
        this.client = createInsecureClient();
    }

    /**
     * Creates an HttpClient that trusts all SSL certificates.
     * Not for production, but fine for this course project demo.
     */
    private HttpClient createInsecureClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
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

    public Quote fetchRandomQuote() throws IOException, InterruptedException {
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
    }

    // Very simple helper: looks for "field":"value"
    private String extractField(String json, String field) {
        String key = "\"" + field + "\":\"";
        int start = json.indexOf(key);
        if (start == -1) return "";
        start += key.length();
        int end = json.indexOf('"', start);
        if (end == -1) return "";
        return json.substring(start, end);
    }

    /**
     * DAO for task data implemented using a File to persist the data.
     */
    public static class FileTaskDataAccessObject implements TaskDataAccessInterface {

        private static final String HEADER = "username,id,title,description,date,course,completed";

        private final File csvFile;
        private final Map<String, Integer> headers = new LinkedHashMap<>();
        private final Map<String, List<Task>> userTasks = new HashMap<>();
        private String currentUsername;
        private int nextId = 1;

        /**
         * Construct this DAO for saving to and reading from a local file.
         * @param csvPath the path of the file to save to
         * @throws RuntimeException if there is an IOException when accessing the file
         */
        public FileTaskDataAccessObject(String csvPath) {
            csvFile = new File(csvPath);

            // Initialize headers
            headers.put("username", 0);
            headers.put("id", 1);
            headers.put("title", 2);
            headers.put("description", 3);
            headers.put("date", 4);
            headers.put("course", 5);
            headers.put("completed", 6);

            if (csvFile.length() == 0) {
                forceSave();
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                    final String header = reader.readLine();

                    if (!header.equals(HEADER)) {
                        throw new RuntimeException(String.format("header should be:%n%s%nbut was:%n%s", HEADER, header));
                    }

                    String row;
                    while ((row = reader.readLine()) != null) {
                        final String[] col = row.split(",");
                        final String username = col[headers.get("username")];
                        final int id = Integer.parseInt(col[headers.get("id")]);
                        final String title = col[headers.get("title")];
                        final String description = col[headers.get("description")];
                        final LocalDate date = LocalDate.parse(col[headers.get("date")]);
                        final String course = col[headers.get("course")];
                        final boolean completed = Boolean.parseBoolean(col[headers.get("completed")]);

                        final Task task = new Task(id, title, description, date, course);
                        if (completed) {
                            task.markCompleted();
                        }

                        // Add task to the user's task list
                        userTasks.computeIfAbsent(username, k -> new ArrayList<>()).add(task);

                        // Track the highest ID
                        if (id >= nextId) {
                            nextId = id + 1;
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        public void forceSave() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
                writer.write(String.join(",", headers.keySet()));
                writer.newLine();

                for (Map.Entry<String, List<Task>> entry : userTasks.entrySet()) {
                    String username = entry.getKey();
                    for (Task task : entry.getValue()) {
                        final String line = String.format("%s,%d,%s,%s,%s,%s,%s",
                                username,
                                task.getId(),
                                task.getTitle(),
                                task.getDescription(),
                                task.getDate().toString(),
                                task.getCourse(),
                                task.isCompleted());
                        writer.write(line);
                        writer.newLine();
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public void saveTask(Task task) {
            if (currentUsername == null) {
                throw new IllegalStateException("No user is currently logged in");
            }

            List<Task> tasks = userTasks.computeIfAbsent(currentUsername, k -> new ArrayList<>());

            // Remove existing task with same ID if it exists (for updates)
            tasks.removeIf(t -> t.getId() == task.getId());

            // Add the new/updated task
            tasks.add(task);
            forceSave();
        }


        @Override
        public List<Task> getAllTasks() {
            if (currentUsername == null) {
                return new ArrayList<>();
            }

            List<Task> tasks = userTasks.get(currentUsername);
            return tasks != null ? new ArrayList<>(tasks) : new ArrayList<>();
        }


        public void setCurrentUsername(String username) {
            this.currentUsername = username;
        }

        public String getCurrentUsername() {
            return currentUsername;
        }

        public int getNextId() {
            return nextId++;
        }

    }
}
