package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.GameData;
import javax.xml.crypto.Data;
import java.sql.SQLException;
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

        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem with createAuth");
        }
    }

    public GameData getGame(Integer id) throws DataAccessException{
        String sqlCommand = """
                SELECT * FROM GameData WHERE gameID = ?;
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.setInt(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Integer gameID = resultSet.getInt("gameID");
                    String whiteUsername = resultSet.getString("whiteUsername");
                    String blackUsername = resultSet.getString("blackUsername");
                    String gameName = resultSet.getString("gameName");
                    String jsonGame = resultSet.getString("game");

                    ChessGame game = gson.fromJson(jsonGame, ChessGame.class);

                    return new GameData(gameID, whiteUsername, blackUsername,gameName, game);
                } else {
                    return null;
                }
            }
        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem retrieving authentication data");
        }
    }

    public Collection<GameData> listGames() throws DataAccessException {

    }

    public void updateGame(Integer id, GameData data) throws DataAccessException {

    }

    public void clear() throws DataAccessException {
        String sqlCommand = """
                DELETE FROM GameData;
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem deleting authToken from Database");
        }
    }
}
