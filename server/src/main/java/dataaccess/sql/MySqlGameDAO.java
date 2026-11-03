package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.GameData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MySqlGameDAO {

    private final Gson gson = new Gson();

    public void createGame(GameData data) throws DataAccessException {
        String sqlCommand = """
                INSERT INTO GameData (gameID, whiteUsername, blackUsername, gameName, game)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.setInt(1, data.gameID());
            statement.setString(2, data.whiteUsername());
            statement.setString(3, data.blackUsername());
            statement.setString(4, data.gameName());

            String jsonGame = gson.toJson(data.game());
            statement.setString(5, jsonGame);

            statement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem creating game", error);
        }
    }

    public GameData getGame(Integer id) throws DataAccessException{
        String sqlCommand = """
                SELECT * FROM GameData WHERE gameID = ?;
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.setInt(1, id);
            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    Integer gameID = result.getInt("gameID");
                    String whiteUsername = result.getString("whiteUsername");
                    String blackUsername = result.getString("blackUsername");
                    String gameName = result.getString("gameName");
                    String jsonGame = result.getString("game");

                    ChessGame game = gson.fromJson(jsonGame, ChessGame.class);

                    return new GameData(gameID, whiteUsername, blackUsername,gameName, game);
                } else {
                    return null;
                }
            }
        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem retrieving authentication data", error);
        }
    }

    public Collection<GameData> listGames() throws DataAccessException {
        String sqlCommand = """
                SELECT * FROM GameData;
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand);
             var result = statement.executeQuery()) {
            Collection<GameData> games = new ArrayList<>();

            while (result.next()) {
                Integer gameID = result.getInt("gameID");
                String whiteUsername = result.getString("whiteUsername");
                String blackUsername = result.getString("blackUsername");
                String gameName = result.getString("gameName");

                String jsonGame = result.getString("game");
                ChessGame game = gson.fromJson(jsonGame, ChessGame.class);

                GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                games.add(gameData);
            }
            return games;


        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem retrieving listGames", error);
        }
    }

    public void updateGame(Integer id, GameData data) throws DataAccessException {
        String sqlCommand = """
                UPDATE GameData
                SET whiteUsername = ?,
                    blackUsername = ?,
                    gameName = ?,
                    game = ?
                WHERE gameID = ?;
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.setString(1, data.whiteUsername());
            statement.setString(2, data.blackUsername());
            statement.setString(3, data.gameName());

            String jsonGame = gson.toJson(data.game());
            statement.setString(4, jsonGame);

            statement.setInt(5, id);

            statement.executeUpdate();

        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem updating the game", error);
        }
    }

    public void clear() throws DataAccessException {
        String sqlCommand = """
                DELETE FROM GameData;
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem clearing GameData", error);
        }
    }
}
