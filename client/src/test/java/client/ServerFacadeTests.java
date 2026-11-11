package client;

import dataaccess.DataAccessException;
import dataaccess.sql.MySqlAuthDAO;
import dataaccess.sql.MySqlGameDAO;
import dataaccess.sql.MySqlUserDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static MySqlUserDAO userDAO;
    private static MySqlGameDAO gameDAO;
    private static MySqlAuthDAO authDAO;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDatabase() throws DataAccessException {
        userDAO = new MySqlUserDAO();
        gameDAO = new MySqlGameDAO();
        authDAO = new MySqlAuthDAO();

        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }

    @Test
    public void registerSuccess() throws IOException, InterruptedException {
        AuthData authData = facade.register("bozo", "bozopassword", "bozo@yahoo.com");
        assertNotNull(authData);
        assertEquals("bozo", authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    public void registerFailure() throws IOException, InterruptedException {
        facade.register("bozo", "bozopassword", "bozo@yahoo.com");
        assertThrows(RuntimeException.class, () -> {facade.register("bozo", "bozopassword", "bozo@yahoo.com");});
    }

    @Test
    public void loginSuccess() throws IOException, InterruptedException {
        facade.register("bozo", "bozopassword", "bozo@yahoo.com");
        AuthData authData = facade.login("bozo", "bozopassword");
        assertEquals("bozo", authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    public void loginFailure() throws IOException, InterruptedException {
        assertThrows(RuntimeException.class, () -> {facade.login("bozo", "bozopassword");});
    }

    @Test
    public void logoutSuccess() throws IOException, InterruptedException {
        facade.register("bozo", "bozopassword", "bozo@yahoo.com");
        AuthData authData = facade.login("bozo", "bozopassword");
        assertDoesNotThrow(() -> facade.logout(authData.authToken()));
    }

    @Test
    public void createGameSuccess() throws IOException, InterruptedException {
        facade.register("bozo", "bozopassword", "bozo@yahoo.com");
        AuthData authData = facade.login("bozo", "bozopassword");
        int gameID = facade.createGame(authData.authToken(), "im not a smurf i promise");
        assertTrue(gameID > 0);
    }

    @Test
    public void listGamesSuccess() throws IOException, InterruptedException {
        facade.register("bozo", "bozopassword", "bozo@yahoo.com");
        AuthData authData = facade.login("bozo", "bozopassword");
        facade.createGame(authData.authToken(), "squid game");
        facade.createGame(authData.authToken(), "Go is better than Chess");
        Collection<GameData> games = facade.listGames(authData.authToken());
        assertFalse(games.isEmpty());
        for (GameData game : games) {
            assertNotNull(game.gameName());
        }
    }

}
