package service;

import chess.ChessGame;
import dataaccess.Auth.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.Game.GameDAO;
import model.GameData;
import service.Exceptions.AlreadyTakenException;
import service.Exceptions.BadRequestException;
import service.Exceptions.UnauthorizedException;
import service.Records.CreateGameResult;
import service.Records.CreateGameRequest;
import service.Records.JoinGameRequest;
import service.Records.ListGamesResult;

import java.util.Objects;
import java.util.Random;

public class GameService {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesResult listGames(String authToken) throws UnauthorizedException, DataAccessException {
        // 401 status handling
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("The token does not exist");
        }
        return new ListGamesResult(gameDAO.listGames());
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest request) throws UnauthorizedException, DataAccessException {
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("The token does not exist");
        }

        // Creating the gameID
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        int gameID = random.nextInt(10000);

        // Creating an instance of ChessGame
        ChessGame game = new ChessGame();

        GameData gameData = new GameData(gameID, null, null, request.gameName(), game);
        gameDAO.createGame(gameData);
        return new CreateGameResult(gameID);
    }

    public void joinGame(String authToken, JoinGameRequest request) throws UnauthorizedException, AlreadyTakenException, DataAccessException, BadRequestException {
        // 401 status handling
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("The token does not exist");
        }

        // 400 status handling
        if (!Objects.equals(request.playerColor(), "WHITE") && !Objects.equals(request.playerColor(), "BLACK")) {
            throw new BadRequestException("playerColor must be WHITE or BLACK");
        }

        GameData gameData = gameDAO.getGame(request.gameID());

        // 400 status handling
        if (gameData == null) {
            throw new BadRequestException("Game not found");
        }

        String username = authDAO.getAuth(authToken).username();

        if (request.playerColor().equals("WHITE")) {
            // 403 status handling
            if (gameData.whiteUsername() != null) {
                throw new AlreadyTakenException("White player slot already taken");
            }

            GameData updatedGame = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
            gameDAO.updateGame(request.gameID(), updatedGame);
        } else {
            // 403 status handling
            if (gameData.blackUsername() != null) {
                throw new AlreadyTakenException("Black player slot already taken");
            }

            GameData updatedGame = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
            gameDAO.updateGame(request.gameID(), updatedGame);
        }
    }
}
