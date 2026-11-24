package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public final class FriendRequestService {

    public enum Result {
        SENT, NOT_FOUND, SELF, EXISTS, SIGN_IN, INVALID_USERNAME
    }

    private final HttpClient http = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(8))
        .build();

    private final URI endpoint;
    private final String publishableKey;
    private final Supplier<String> accessToken;

    public FriendRequestService(URI supabaseUrl,
                                String publishableKey,
                                Supplier<String> accessToken) {
        this.endpoint = supabaseUrl.resolve("/rest/v1/rpc/send_friend_request");
        this.publishableKey = publishableKey;
        this.accessToken = accessToken;
    }

    public CompletableFuture<Result> send(String username) {
        if (username.isBlank() || username.length() > 128
                || username.chars().anyMatch(Character::isISOControl)) {
            return CompletableFuture.completedFuture(Result.INVALID_USERNAME);
        }

        String token = accessToken.get();
        if (token == null || token.isBlank()) {
            return CompletableFuture.completedFuture(Result.SIGN_IN);
        }

        String escaped = username.replace("\\", "\\\\").replace("\"", "\\\"");
        String body = "{\"target_username\":\"" + escaped + "\"}";

        HttpRequest request = HttpRequest.newBuilder(endpoint)
            .timeout(Duration.ofSeconds(12))
            .header("apikey", publishableKey)
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

        return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(FriendRequestService::readResult);
    }

    private static Result readResult(HttpResponse<String> response) {
        if (response.statusCode() == 401 || response.statusCode() == 403) {
            return Result.SIGN_IN;
        }

        if (response.statusCode() != 200) {
            throw new IllegalStateException("Request failed: " + response.statusCode());
        }

        return switch (response.body().strip()) {
            case "\"sent\"" -> Result.SENT;
            case "\"not_found\"" -> Result.NOT_FOUND;
            case "\"self\"" -> Result.SELF;
            case "\"exists\"" -> Result.EXISTS;
            case "\"sign_in\"" -> Result.SIGN_IN;
            case "\"invalid_username\"" -> Result.INVALID_USERNAME;
            default -> throw new IllegalStateException("Unexpected response.");
        };
    }
}