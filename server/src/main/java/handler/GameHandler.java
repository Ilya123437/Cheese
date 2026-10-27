package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.Exceptions.AlreadyTakenException;
import service.Exceptions.BadRequestException;
import service.Exceptions.UnauthorizedException;
import service.GameService;
import io.javalin.http.Context;
import service.Records.*;

import java.util.Map;

public class GameHandler {
    private final GameService gameService;
    private final Gson serializer = new Gson();

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void listGames(Context ctx) {
        String authToken = ctx.header("Authorization");

        try {
            ListGamesResult result = gameService.listGames(authToken);
            ctx.status(200);
            ctx.json(serializer.toJson(result));
        } catch (UnauthorizedException error) {
            ctx.status(401);
            ctx.json(Map.of("message", error.getMessage()));
        } catch (DataAccessException error) {
            ctx.status(500);
            ctx.json(Map.of("message", error.getMessage()));
        }
    }

    public void createGame(Context ctx) {
        String authToken = ctx.header("Authorization");

        try {
            CreateGameRequest request = serializer.fromJson(ctx.body(),CreateGameRequest.class);
            CreateGameResult result = gameService.createGame(authToken, request);
            ctx.status(200);
            ctx.json(serializer.toJson(result));
        } catch (BadRequestException error) {
            ctx.status(400);
            ctx.result(String.format("""
                {"message": "%s"}""",error.getMessage()));
        } catch (UnauthorizedException error) {
            ctx.status(401);
            ctx.result(String.format("""
                {"message": "%s"}""",error.getMessage()));
        } catch (DataAccessException error) {
            ctx.status(500);
            ctx.result(String.format("""
                {"message": "%s"}""",error.getMessage()));
        }
    }

    public void joinGame(Context ctx) {
        String authToken = ctx.header("Authorization");

        try {
            JoinGameRequest request = serializer.fromJson(ctx.body(),JoinGameRequest.class);
            gameService.joinGame(authToken, request);
            ctx.status(200);
        } catch (BadRequestException error) {
            ctx.status(400);
            ctx.result(String.format("""
                {"message": "%s"}""",error.getMessage()));
        } catch (UnauthorizedException error) {
            ctx.status(401);
            ctx.result(String.format("""
                {"message": "%s"}""",error.getMessage()));
        } catch (AlreadyTakenException error) {
            ctx.status(403);
            ctx.result(String.format("""
                {"message": "%s"}""",error.getMessage()));
        } catch (DataAccessException error) {
            ctx.status(500);
            ctx.result(String.format("""
                {"message": "%s"}""",error.getMessage()));
        }
    }

}
