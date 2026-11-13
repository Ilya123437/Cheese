package client;

import chess.ChessGame;
import client.Exceptions.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.AuthData;
import model.GameData;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.Map;

public class ServerFacade {

    private final HttpClient httpClient;
    private final String serverUrl;
    private final Gson gson;

    public ServerFacade (Integer port) {
        this.httpClient = HttpClient.newHttpClient();
        this.serverUrl = "http://localhost:" + port;
        this.gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .create();
    }

    public AuthData register(String username, String password, String email) throws DataAccessException, IOException, InterruptedException, AlreadyTakenException {
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
        } else if (httpResponse.statusCode() == 403) {
            throw new AlreadyTakenException("Username already taken");
        } else {
            throw handleError(httpResponse.statusCode());
        }
    }

    public AuthData login(String username, String password) throws DataAccessException, AlreadyTakenException, IOException, InterruptedException{
        Map<String, String> request = Map.of(
                "username", username,
                "password", password
        );
        String json = gson.toJson(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/session"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() == 200) {
            return gson.fromJson(httpResponse.body(), AuthData.class);
        } else if (httpResponse.statusCode() == 401) {
          throw new InvalidCredentialsException("Invalid username or password");
        } else {
            throw handleError(httpResponse.statusCode());
        }
    }

    public void logout(String authToken) throws AlreadyTakenException, IOException, InterruptedException, DataAccessException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/session"))
                .header("Authorization", authToken)
                .DELETE()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() == 200) {
            return;
        } else {
            throw handleError(httpResponse.statusCode());
        }
    }

    public int createGame(String authToken, String gameName) throws DataAccessException, AlreadyTakenException, IOException, InterruptedException{
        Map<String, String> request = Map.of(
                "gameName", gameName
        );

        String json = gson.toJson(request);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/game"))
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() == 200) {
            CreateGameResult response = gson.fromJson(httpResponse.body(), CreateGameResult.class);
            return response.gameID();
        } else {
            throw handleError(httpResponse.statusCode());
        }
    }

    public Collection<GameData> listGames(String authToken) throws AlreadyTakenException, DataAccessException, IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/game"))
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            ListGamesResult response = gson.fromJson(httpResponse.body(), ListGamesResult.class);
            return response.games();
        } else {
            throw handleError(httpResponse.statusCode());
        }
    }

    public void joinGame(String authToken, Integer gameID, ChessGame.TeamColor color) throws DataAccessException, AlreadyTakenException, IOException, InterruptedException {
        Map<String, Object> request = Map.of(
                "gameID", gameID,
                "playerColor", color
        );
        String json = gson.toJson(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/game"))
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            return;
        } else {
            throw handleError(httpResponse.statusCode());
        }
    }

    private RuntimeException handleError(int statusCode) throws DataAccessException, AlreadyTakenException{
        if (statusCode == 400) {
            return new BadRequestException("Bad Request");
        } else if (statusCode == 401) {
            return new UnauthorizedException("Unauthorized");
        } else if (statusCode == 403) {
            return new AlreadyTakenException("Already taken");
        } else if (statusCode == 500) {
            return new DataAccessException("Server error");
        } else {
            return new RuntimeException("Unhandled status code" + statusCode);
        }
    }

}
