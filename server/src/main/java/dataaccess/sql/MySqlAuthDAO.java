package dataaccess.sql;

import dataaccess.Auth.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDAO {
    public void createAuth(AuthData data) throws DataAccessException {
        String sqlCommand = """
                INSERT INTO AuthData (authToken, username)
                VALUES (?,?)
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.setString(1, data.authToken());
            statement.setString(2, data.username());
            statement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem with createAuth", error);
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException{
        String sqlCommand = """
                SELECT * FROM AuthData WHERE authToken = ?;
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.setString(1, authToken);
            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    String token = result.getString("authToken");
                    String username = result.getString("username");

                    return new AuthData(token, username);
                } else {
                    return null;
                }
            }
        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem retrieving authentication data", error);
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        String sqlCommand = """
                DELETE FROM AuthData WHERE authToken = ?;
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.setString(1, authToken);
            statement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem deleting authToken from Database", error);
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
            throw new DataAccessException("Error: Problem clearing AuthData", error);
        }

    }
}
