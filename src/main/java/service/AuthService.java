package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

public class AuthService {

    private static final String URL =
        "https://hcdpnqaekllmmboyhoqb.supabase.co";

    private static final String API_KEY =
        System.getenv("SUPABASE_PUBLISHABLE_KEY");

    private final HttpClient client = HttpClient.newHttpClient();

    public int testConnection() throws Exception {

        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException(
                "SUPABASE_PUBLISHABLE_KEY is missing"
            );
        }

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL + "/auth/v1/health"))
            .header("apikey", API_KEY)
            .GET()
            .build();

        HttpResponse<String> response = client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        );

        return response.statusCode();
    }

    public int signUp(String email, String password, String username, String firstName, String lastName) throws Exception{

        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException("Supabase API key is missing");
        }

        String body = """
        {
            "email": "%s",
            "password": "%s",
            "data": {
                "username": "%s",
                "first_name": "%s",
                "last_name": "%s"
            }
        }
        """.formatted(
            escapeJson(email),
            escapeJson(password),
            escapeJson(username),
            escapeJson(firstName),
            escapeJson(lastName)
        );

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL + "/auth/v1/signup"))
        .header("apikey", API_KEY)
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(body))
        .build();

    HttpResponse<String> response = client.send(
        request,
        HttpResponse.BodyHandlers.ofString()
    );

    System.out.println("Signup status: " + response.statusCode());

    return response.statusCode();
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }

    public boolean login(String username, String password) throws Exception {

        String body = """
            {
                "username": "%s",
                "password": "%s"
            }
            """.formatted(
                escapeJson(username),
                escapeJson(password)
            );

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL + "/functions/v1/username-login"))
            .header("apikey", API_KEY)
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(body))
            .build();

        HttpResponse<String> response = client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        );
        System.out.println("Login status: " + response.statusCode());

        if (response.statusCode() != 200) {
            System.out.println(response.body());
        }
        return response.statusCode() == 200;
    }
}
