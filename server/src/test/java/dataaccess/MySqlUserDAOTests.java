package dataaccess;

import dataaccess.User.UserDAO;
import dataaccess.sql.MySqlUserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlUserDAOTests {
    private MySqlUserDAO userDAO;

    @BeforeAll
    static void setupDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }

    @BeforeEach
    void clear() throws DataAccessException {
        userDAO = new MySqlUserDAO();
        userDAO.clear();
    }

    @Test
    void createUserTest() throws DataAccessException {
        UserData userData = new UserData("bozo", "bozopassword", "bozo@yahoo.com");
        userDAO.createUser(userData);
        UserData user = userDAO.getUser("bozo");
        assertNotNull(user);
        assertEquals("bozo", user.username());
        assertEquals("bozo@yahoo.com", user.email());
        // need to check the password somehow
    }

    @Test
    void getUserDNETest() throws DataAccessException {
        assertNull(userDAO.getUser("DNE"));
    }

    @Test
    void clearTest() throws DataAccessException {
        UserData userData = new UserData("bozo", "bozopassword", "bozo@yahoo.com");
        userDAO.createUser(userData);
        userDAO.clear();
        assertNull(userDAO.getUser("bozo"));
    }
}
