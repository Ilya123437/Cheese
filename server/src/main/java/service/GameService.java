package service;

import chess.ChessGame;
import dataaccess.Auth.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.Game.GameDAO;
import model.AuthData;
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
import service.util.RequestValidator;

public class GameService {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesResult listGames(String authToken) throws UnauthorizedException, DataAccessException {
        // 401 status handling
        authorization(authToken);
        return new ListGamesResult(gameDAO.listGames());
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest request) throws BadRequestException, UnauthorizedException, DataAccessException {
        // 401 status handling
        authorization(authToken);
        RequestValidator.validateRequiredFields(request.gameName());

        // Creating the gameID
        Random random = new Random();
        int gameID = random.nextInt(Integer.MAX_VALUE);

        while (gameDAO.getGame(gameID) != null) {
            gameID = random.nextInt(Integer.MAX_VALUE);
        }

        // Creating an instance of ChessGame
        ChessGame game = new ChessGame();

        GameData gameData = new GameData(gameID, null, null, request.gameName(), game);
        gameDAO.createGame(gameData);
        return new CreateGameResult(gameID);
    }

    public void joinGame(String authToken, JoinGameRequest request) throws UnauthorizedException, AlreadyTakenException, DataAccessException, BadRequestException {
        // 401 status handling
        AuthData authData = authDAO.getAuth((authToken));
        if (authData == null) {
            throw new UnauthorizedException("Error: The token does not exist");
        }

        // 400 status handling
        if (request == null || request.gameID() == null) {
            throw new BadRequestException("Error: gameID is missing");
        }
        if (!Objects.equals(request.playerColor(), "WHITE") && !Objects.equals(request.playerColor(), "BLACK")) {
            throw new BadRequestException("Error: playerColor must be WHITE or BLACK");
        }
        GameData gameData = gameDAO.getGame(request.gameID());
        if (gameData == null) {
            throw new BadRequestException("Error: Game not found");
        }

        String username = authData.username();
        if (request.playerColor().equals("WHITE")) {
            // 403 status handling
            if (gameData.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: White player slot already taken");
            }

            GameData updatedGame = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
            gameDAO.updateGame(request.gameID(), updatedGame);
        } else {
            // 403 status handling
            if (gameData.blackUsername() != null) {
                throw new AlreadyTakenException("Error: Black player slot already taken");
            }

            GameData updatedGame = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
            gameDAO.updateGame(request.gameID(), updatedGame);
        }
    }

    private void authorization(String authToken) throws UnauthorizedException{
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("Error: The token does not exist");
        }
    }
}
