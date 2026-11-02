package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class MySqlUserDAO {

    public void createUser(UserData data) throws DataAccessException {
        String sqlCommand = """
                INSERT INTO UserData (username, password, email)
                VALUES (?, ?, ?)
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.setString(1, data.username());
            statement.setString(2, data.password());
            statement.setString(3, data.email());
            // need to include function in here to hash the password and then store it

        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem with creating the User");
        }
    }

    public UserData getUser(String username) {

    }

    public void clear() throws DataAccessException {
        String sqlCommand = """
                DELETE FROM UserData;
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem clearing UserData");
        }
    }
}
