package ui;


import chess.ChessGame;
import client.ServerFacade;
import model.AuthData;
import model.GameData;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;
import java.util.Scanner;

public class PostLoginUI {
    private final ServerFacade facade;
    private final Scanner scanner;
    private AuthData authData;
    private Collection<GameData> games;

    public PostLoginUI(ServerFacade facade, Scanner scanner, AuthData authData) throws IOException, InterruptedException{
        this.facade = facade;
        this.scanner = scanner;
        this.authData = authData;
        this.games = facade.listGames(authData.authToken());
    }

    public void run() throws IOException, InterruptedException{
        boolean loggedOut = false;
        String commands =
                "- help\n" +
                        "- logout\n" +
                        "- create game\n" +
                        "- list games\n" +
                        "- play game\n" +
                        "- observe game\n";
        while (!loggedOut) {
            print(commands);
            String input = this.scanner.nextLine();
            switch (input.toLowerCase(Locale.ROOT)) {
                case "help" -> print(commands);
                case "create game" -> createGame();
                case "list games" -> listGames();
                case "join game" -> joinGame();
                case "observe game" -> observeGame();
                case "logout" -> {
                    logout();
                    loggedOut = true;
                    PreLoginUI preLoginUI = new PreLoginUI(this.facade, this.scanner);
                    preLoginUI.run();
                }
                default -> print("Invalid command");
            }
        }
    }

    public void logout() throws IOException, InterruptedException {
        facade.logout(authData.authToken());
    }

    public void createGame() throws IOException, InterruptedException {
        print("Enter a name for the game: ");
        String gameName = this.scanner.nextLine();
        facade.createGame(authData.authToken(), gameName);
    }

    public void listGames() throws IOException, InterruptedException{
        this.games = facade.listGames(authData.authToken());
        int i = 1;
        for (GameData game : games) {
            print(i + "Game: " + game.gameName());
            if (game.whiteUsername() != null) {
                print("White: " + game.whiteUsername());
            } else {
                print("White: Available");
            }
            if (game.blackUsername() != null) {
                print("Black: " + game.blackUsername());
            } else {
                print("Black: Available");
            }
        }
    }

    public void joinGame() throws IOException, InterruptedException {
        boolean joined = false;
        while (!joined) {
            print("Type the number for which game to play (1 - " + games.size() + "): ");
            String input = scanner.nextLine();

            int selection;

            try {
                selection = Integer.parseInt(input);
            } catch (NumberFormatException error) {
                print("That is not a number, try again");
                continue;
            }

            if (selection <= games.size() && selection >= 1) {
                GameData game = findGame(selection);
                boolean whiteAvailable = game.whiteUsername() == null;
                boolean blackAvailable = game.blackUsername() == null;

                ChessGame.TeamColor color;

                if (!whiteAvailable && !blackAvailable) {
                    print("This game has no spots available, pick another game");
                    continue;
                } else if (whiteAvailable && blackAvailable) {
                    print("Choose a color: white or black");
                    String choice = scanner.nextLine().toLowerCase(Locale.ROOT);

                    if (choice.equals("white")) {
                        color = ChessGame.TeamColor.WHITE;

                    } else if (choice.equals("black")) {
                        color = ChessGame.TeamColor.BLACK;
                    } else {
                        print("Invalid color");
                        continue;
                    }
                } else if (whiteAvailable) {
                    color = ChessGame.TeamColor.WHITE;
                } else {
                    color = ChessGame.TeamColor.BLACK;
                }

                try {
                    facade.joinGame(authData.authToken(), game.gameID(), color);
                    print("Successfully joined as " + color);
                    joined = true;
                } catch (RuntimeException error) {
                    print("Could not join the game.");
                }

            } else {
                print("Not on the list of games, try again");
            }


        }
    }

    public void observeGame() {

    }

    public void print(String thingToPrint) {
        System.out.print(thingToPrint);
    }

    public GameData findGame(int index) {
        int i = 1;
        for (GameData game : this.games) {
            if (i == index) {
                return game;
            }
            i++;
        }
        return null;
    }
}
