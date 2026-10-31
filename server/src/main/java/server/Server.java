package server;

import dataaccess.Auth.AuthDAO;
import dataaccess.Auth.MemoryAuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.Game.GameDAO;
import dataaccess.Game.MemoryGameDAO;
import dataaccess.User.MemoryUserDAO;
import dataaccess.User.UserDAO;
import handler.ClearHandler;
import handler.GameHandler;
import handler.SessionHandler;
import handler.UserHandler;
import io.javalin.*;
import service.ClearService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
        } catch (DataAccessException error) {
            System.out.println("Error : " + error);
        }


        // Register your endpoints and exception handlers here.
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        UserService userService = new UserService(userDAO, authDAO);
        GameService gameService = new GameService(authDAO, gameDAO);
        ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);

        UserHandler userHandler = new UserHandler(userService);
        SessionHandler sessionHandler = new SessionHandler(userService);
        GameHandler gameHandler = new GameHandler(gameService);
        ClearHandler clearHandler = new ClearHandler(clearService);

        javalin.post("/user", userHandler::register);
        javalin.post("/session", sessionHandler::login);
        javalin.delete("/session", sessionHandler::logout);
        javalin.get("/game", gameHandler::listGames);
        javalin.post("/game", gameHandler::createGame);
        javalin.put("/game", gameHandler::joinGame);
        javalin.delete("/db", clearHandler::clear);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
