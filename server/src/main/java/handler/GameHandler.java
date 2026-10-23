package handler;

import dataaccess.DataAccessException;
import service.Exceptions.UnauthorizedException;
import service.GameService;
import io.javalin.http.Context;
import service.Records.CreateGameRequest;
import service.Records.CreateGameResult;
import service.Records.RegisterRequest;
import service.Records.RegisterResult;

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

            CreateGameResult result = gameService.createGame(new CreateGameRequest(
                    request.authToken(),
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

}
