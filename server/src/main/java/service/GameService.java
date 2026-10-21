package service;

import chess.ChessGame;
import dataaccess.Auth.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.Game.GameDAO;
import dataaccess.User.UserDAO;
import model.GameData;
import org.jetbrains.annotations.NotNull;
import service.Exceptions.AlreadyTakenException;
import service.Exceptions.BadRequestException;
import service.Exceptions.UnauthorizedException;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;

public class GameService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Collection<GameData> listGames(String authToken) throws UnauthorizedException {
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("The token does not exist");
        }
        return gameDAO.listGames();
    }

    public void createGame(String authToken, String gameName) throws UnauthorizedException, DataAccessException {
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("The token does not exist");
        }

        // Creating the gameID
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        int gameID = random.nextInt(10000);

        // Creating an instance of ChessGame
        ChessGame game = new ChessGame();

        GameData gameData = new GameData(gameID, null, null, gameName, game);
        gameDAO.createGame(gameData);
    }

    public void joinGame(String playerColor, @NotNull Integer gameID, String authToken) throws UnauthorizedException, AlreadyTakenException, DataAccessException, BadRequestException {
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("The token does not exist");
        }
        if (!Objects.equals(playerColor, "WHITE") && !Objects.equals(playerColor, "BLACK")) {
            throw new BadRequestException("playerColor must be WHITE or BLACK");
        }

        GameData gameData = gameDAO.getGame(gameID);

        if (gameData == null) {
            throw new DataAccessException("Game not found");
        }

        String username = authDAO.getAuth(authToken).username();

        if (playerColor.equals("WHITE")) {
            if (gameData.whiteUsername() != null) {
                throw new AlreadyTakenException("White player slot already taken");
            }

            GameData updatedGame = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
            gameDAO.updateGame(gameID, updatedGame);
        } else {
            if (gameData.blackUsername() != null) {
                throw new AlreadyTakenException("Black player slot already taken");
            }

            GameData updatedGame = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
            gameDAO.updateGame(gameID, updatedGame);
        }
    }


}
