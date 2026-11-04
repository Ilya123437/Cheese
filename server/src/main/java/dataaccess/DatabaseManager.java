package dataaccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        loadPropertiesFromResources();
    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }

    static public void createTables() throws DataAccessException{
        var authDataTable = "CREATE TABLE IF NOT EXISTS AuthData(\n" +
                "    authToken VARCHAR(255) PRIMARY KEY ,\n" +
                "    username VARCHAR(255) NOT NULL\n" +
                ");";
        var userDataTable = "CREATE TABLE IF NOT EXISTS UserData(\n" +
                "    username VARCHAR(255) PRIMARY KEY, \n" +
                "    password VARCHAR(255) NOT NULL, \n" +
                "    email VARCHAR(255) NOT NULL);";
        var gameDataTable = "CREATE TABLE IF NOT EXISTS GameData(\n" +
                "    gameID INT PRIMARY KEY, \n" +
                "    whiteUsername VARCHAR(255) ,\n" +
                "    blackUsername VARCHAR(255) ,\n" +
                "    gameName VARCHAR(255) ,\n" +
                "    game TEXT);";

        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
            conn.setCatalog(databaseName);
            var preparedStatementAuth = conn.prepareStatement(authDataTable);
            preparedStatementAuth.executeUpdate();

            var preparedStatementUser = conn.prepareStatement(userDataTable);
            preparedStatementUser.executeUpdate();

            var preparedStatementGame = conn.prepareStatement(gameDataTable);
            preparedStatementGame.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("failed to create tables", ex);
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("Error: failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }
}
