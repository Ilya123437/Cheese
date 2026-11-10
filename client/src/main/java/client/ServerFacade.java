package client;

import com.google.gson.Gson;
import model.AuthData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ServerFacade {

    private final HttpClient httpClient;
    private final String serverUrl;
    private final Gson gson;

    public ServerFacade (Integer port) {
        this.httpClient = HttpClient.newHttpClient();
        this.serverUrl = "http://localhost:" + port;
        this.gson = new Gson();
    }

    public AuthData register(String username, String password, String email) throws IOException, InterruptedException {
        Map<String, String> request = Map.of(
                "username", username,
                "password", password,
                "email", email
        );
        String json = gson.toJson(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            return gson.fromJson(httpResponse.body(), AuthData.class);
        } else {
            throw new RuntimeException("Error: status code is " + httpResponse.statusCode());
        }
    }

    public AuthData login(String username, String password) throws IOException, InterruptedException{
        Map<String, String> request = Map.of(
                "username", username,
                "password", password
        );
        String json = gson.toJson(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() == 200) {
            return gson.fromJson(httpResponse.body(), AuthData.class);
        } else {
            throw new RuntimeException("Error: status code is " + httpResponse.statusCode());
        }
    }

    public void logout(String authToken) {

    }

    public void listGames(String authToken) {

    }

    public void createGame(String authToken, String gameName) {

    }

    public void joinGame(String authToken, Integer gameID, String color) {

    }

}
