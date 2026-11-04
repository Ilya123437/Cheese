package dataaccess;

import chess.ChessGame;
import dataaccess.sql.MySqlGameDAO;
import model.GameData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MySqlGameDAOTests {
    private MySqlGameDAO gameDAO;

    @BeforeAll
    static void setupDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }

    @BeforeEach
    void clear() throws DataAccessException {
        gameDAO = new MySqlGameDAO();
        gameDAO.clear();
    }

    @Test
    void createGameTest() throws DataAccessException {
        GameData gameData = new GameData(67, "garry kasporov", "deep blue", "humanity failed", new ChessGame());
        gameDAO.createGame(gameData);
        assertEquals(gameDAO.getGame(67), gameData);
    }

    @Test
    void getGameDNETest() throws DataAccessException {
        assertNull(gameDAO.getGame(67));
    }

    @Test
    void listGamesTest() throws DataAccessException {
        GameData gameData = new GameData(67, "garry kasporov", "deep blue", "humanity failed", new ChessGame());
        gameDAO.createGame(gameData);
        GameData gameData1 = new GameData(69, "deep blue", "garry kasporov", "humanity failed", new ChessGame());
        gameDAO.createGame(gameData1);
        assert(gameDAO.listGames().size() == 2);
    }

    @Test
    void updateGameTest() throws DataAccessException {
        GameData gameData = new GameData(67, "garry kasporov", "deep blue", "humanity failed", new ChessGame());
        gameDAO.createGame(gameData);
        GameData gameData1 = new GameData(67, "hans niemann", "magnus carlsen", "incident", new ChessGame());
        gameDAO.updateGame(67, gameData1);
        GameData updatedGame = gameDAO.getGame(67);
        assertEquals(gameData1, updatedGame);
    }

    @Test
    void updateGameDNETest() throws DataAccessException {
        GameData gameData = new GameData(67, "garry kasporov", "deep blue", "humanity failed", new ChessGame());
        assertThrows(DataAccessException.class, () -> gameDAO.updateGame(67, gameData));
    }

    @Test
    void clearTest() throws DataAccessException {
        GameData gameData = new GameData(67, "garry kasporov", "deep blue", "humanity failed", new ChessGame());
        gameDAO.createGame(gameData);
        GameData gameData1 = new GameData(67, "hans niemann", "magnus carlsen", "incident", new ChessGame());
        gameDAO.updateGame(67, gameData1);
        gameDAO.clear();
        assert(gameDAO.listGames().size() == 0);
    }
}
