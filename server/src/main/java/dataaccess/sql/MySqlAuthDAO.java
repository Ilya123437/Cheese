package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.SQLException;

public class MySqlAuthDAO {
    public void createAuth(AuthData data) throws DataAccessException {
        String sqlCommand = """
                INSERT INTO auth (authToken, username)
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
}
