package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class MySqlUserDAO {

    public void createUser(UserData data) throws DataAccessException {
        String sqlCommand = """
                INSERT INTO UserData (username, password, email)
                VALUES (?, ?, ?)
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            String hashedPassword = BCrypt.hashpw(data.password(), BCrypt.gensalt());
            statement.setString(1, data.username());
            statement.setString(2, hashedPassword);
            statement.setString(3, data.email());

            statement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem with creating the User", error);
        }
    }

    public UserData getUser(String username) throws DataAccessException{
        String sqlCommand = """
                SELECT * FROM UserData WHERE username = ?;
                """;

        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.setString(1, username);
            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    String userName = result.getString("username");
                    String password = result.getString("password");
                    String email = result.getString("email");

                    return new UserData(userName, password, email);
                } else {
                    return null;
                }
            }
        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem retrieving user data", error);
        }
    }

    public void clear() throws DataAccessException {
        String sqlCommand = """
                DELETE FROM UserData;
                """;
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlCommand)) {
            statement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("Error: Problem clearing UserData", error);
        }
    }
}
