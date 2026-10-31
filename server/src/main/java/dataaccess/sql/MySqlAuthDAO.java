package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.SQLException;

public class MySqlAuthDAO {
    public void createAuth(AuthData data) throws DataAccessException {
        String sqlCommand = """
                INSERT INTO AuthData (authToken, username)
                VALUES (?,?)
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.setString(1, data.authToken());
            statement.setString(2, data.username());

        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem with createAuth");
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException{
        String sqlCommand = """
                SELECT * FROM AuthData WHERE authToken = ?;
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.setString(1, authToken);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String token = resultSet.getString("authToken");
                    String username = resultSet.getString("username");

                    return new AuthData(token, username);
                } else {
                    return null;
                }
            }
        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem retrieving authentication data");
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException{
        String sqlCommand = """
                DELETE FROM AuthData WHERE authToken = ?;
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.setString(1, authToken);
            statement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem deleting authToken from Database");
        }
    }

    public void clear() throws DataAccessException{
        String sqlCommand = """
                DELETE FROM AuthData;
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem deleting authToken from Database");
        }

    }
}
