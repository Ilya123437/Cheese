package handler;

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

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void listGames(Context ctx) {
        String authToken = ctx.header("Authorization");

        try {
            gameService.listGames(authToken);
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
            CreateGameRequest request = ctx.bodyAsClass(CreateGameRequest.class);

            CreateGameResult result = gameService.createGame(authToken, new CreateGameRequest(
                    request.gameName()
            ));

            ctx.status(200);
            ctx.json(result);

        } catch (UnauthorizedException error) {
            ctx.status(401);
            ctx.json(Map.of("message", error.getMessage()));
        } catch (DataAccessException error) {
            ctx.status(500);
            ctx.json(Map.of("message", error.getMessage()));
        }
    }

    public void joinGame(Context ctx) {
        String authToken = ctx.header("Authorization");

        try {
            JoinGameRequest request = ctx.bodyAsClass(JoinGameRequest.class);
            gameService.joinGame(authToken, request);

            ctx.status(200);

        } catch (BadRequestException error) {
            ctx.status(400);
            ctx.json(Map.of("message", error.getMessage()));
        } catch (UnauthorizedException error) {
            ctx.status(401);
            ctx.json(Map.of("message", error.getMessage()));
        } catch (AlreadyTakenException error) {
            ctx.status(403);
            ctx.json(Map.of("message", error.getMessage()));
        } catch (DataAccessException error) {
            ctx.status(500);
            ctx.json(Map.of("message", error.getMessage()));
        }
    }

}
