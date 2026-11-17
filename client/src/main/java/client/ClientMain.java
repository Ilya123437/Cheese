package client;

import chess.*;
import dataaccess.DataAccessException;
import dataaccess.sql.MySqlAuthDAO;
import dataaccess.sql.MySqlGameDAO;
import dataaccess.sql.MySqlUserDAO;
import ui.PreLoginUI;
import java.io.IOException;
import java.util.Scanner;
import server.Server;

public class ClientMain {
    public static void main(String[] args) throws DataAccessException, IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Server server = new Server();
        var port = server.run(0);
        client.ServerFacade facade = new ServerFacade(port);
        MySqlUserDAO userDAO;
        MySqlGameDAO gameDAO;
        MySqlAuthDAO authDAO;
        userDAO = new MySqlUserDAO();
        gameDAO = new MySqlGameDAO();
        authDAO = new MySqlAuthDAO();
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();

        System.out.println("♕ 240 Chess Client: ");
        PreLoginUI preLoginUI = new PreLoginUI(facade, scanner);
        preLoginUI.run();
    }
}
