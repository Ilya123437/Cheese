package ui;


import chess.ChessGame;
import client.ServerFacade;
import model.AuthData;
import model.GameData;
import java.io.IOException;
import java.util.*;

public class PostLoginUI {
    private final ServerFacade facade;
    private final Scanner scanner;
    private AuthData authData;
    private Collection<GameData> games;

    public PostLoginUI(ServerFacade facade, Scanner scanner, AuthData authData) throws IOException, InterruptedException{
        this.facade = facade;
        this.scanner = scanner;
        this.authData = authData;
        try {
            this.games = facade.listGames(authData.authToken());
        } catch (RuntimeException error) {
            this.games = new ArrayList<>();
        }
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
            print("\nYou are now logged in as " + authData.username() + "\n");
            print(commands);
            print(">>> ");
            String input = this.scanner.nextLine();
            try {
                switch (input.toLowerCase(Locale.ROOT)) {
                    case "help" -> print(commands);
                    case "create game" -> createGame();
                    case "list games" -> listGames();
                    case "play game" -> joinGame();
                    case "observe game" -> observeGame();
                    case "logout" -> {
                        logout();
                        loggedOut = true;
                        PreLoginUI preLoginUI = new PreLoginUI();
                        preLoginUI.run();
                    }
                    default -> print("Invalid command");
                }
            } catch (IOException | InterruptedException error) {
                print("A problem occurred with the server try again\n");
            } catch (RuntimeException error) {
                print("Something went wrong try again\n");
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
        print("\nGame Created\n");
    }

    public void listGames() throws IOException, InterruptedException{
        this.games = facade.listGames(authData.authToken());
        if (games.isEmpty()) {
            print("No games yet. Use 'create game' to make one\n");
            return;
        }

        int i = 1;
        for (GameData game : games) {
            print("\n" + i + ") Game: " + game.gameName() + "\n");
            i++;
            if (game.whiteUsername() != null) {
                print("     White: " + game.whiteUsername());
            } else {
                print("     White: Available");
            }
            if (game.blackUsername() != null) {
                print("     Black: " + game.blackUsername());
            } else {
                print("     Black: Available\n");
            }

        }
    }

    public void joinGame() throws IOException, InterruptedException {
        boolean joined = false;
        this.games = facade.listGames(authData.authToken());
        if (games.isEmpty()) {
            print("No games yet. Use 'create game' to make one\n");
        }


        while (!joined) {
            print("\nIf you want to go back type 'back' for game\n");
            print("Type the number for which game to play (1 - " + games.size() + "): ");
            print("\n>>> ");
            String input = scanner.nextLine();

            if (Objects.equals(input, "back")) {
                return;
            }

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
                    print("Choose a color: white or black\n");
                    print(">>> ");
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
                    print("Successfully joined as " + color + "\n");
                    BoardPrinter.drawBoard(game.game().getBoard(), color);
                    joined = true;
                } catch (RuntimeException error) {
                    print("Could not join the game.");
                }
            } else {
                print("Not on the list of games, try again");
            }
        }
    }

    public void observeGame() throws IOException, InterruptedException{
        this.games = facade.listGames(authData.authToken());
        if (games.isEmpty()) {
            print("No games available to observe\n");
            return;
        }

        while (true) {
            print("If you want to go back type 'back' for game\n");
            print("Type the number for which game to observe (1 - " + games.size() + "): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("back")) {
                return;
            }

            int selection;
            try {
                selection = Integer.parseInt(input);
            } catch (NumberFormatException error) {
                print("That is not a number, try again\n");
                continue;
            }

            if (selection >= 1 && selection <= games.size()) {
                GameData game = findGame(selection);
                BoardPrinter.drawBoard(game.game().getBoard(), ChessGame.TeamColor.WHITE);
                return;
            } else {
                print("Not on the list of games, try again\n");
            }
        }

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
