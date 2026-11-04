package dataaccess;

import dataaccess.sql.MySqlAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.*;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class MySqlAuthDAOTests {
    private MySqlAuthDAO authDAO;

    @BeforeAll
    static void setupDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }

    @BeforeEach
    void clear() throws DataAccessException {
        authDAO = new MySqlAuthDAO();
        authDAO.clear();
    }

    @Test
    void createAuthTest() throws DataAccessException {
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, "bozo");
        authDAO.createAuth(auth);
        AuthData retrievedAuth = authDAO.getAuth(token);
        assertEquals(token, retrievedAuth.authToken());
    }

    @Test
    void getAuthDNETest() throws DataAccessException {
        assertNull(authDAO.getAuth("randomToken"));
    }

    @Test
    void deleteAuthTest() throws DataAccessException {
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, "bozo");
        authDAO.createAuth(auth);
        authDAO.deleteAuth(token);
        assertNull(authDAO.getAuth(token));
    }

    @Test
    void clearTest() throws DataAccessException {
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, "bozo");
        authDAO.createAuth(auth);
        String token1 = UUID.randomUUID().toString();
        AuthData auth1 = new AuthData(token1, "bozo1");
        authDAO.createAuth(auth1);
        authDAO.clear();
        assertNull(authDAO.getAuth(token));
        assertNull(authDAO.getAuth(token1));
    }

    @Test
    void createDuplicateAuthTest() throws DataAccessException {
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, "bozo");
        authDAO.createAuth(auth);
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(auth));
    }
}
