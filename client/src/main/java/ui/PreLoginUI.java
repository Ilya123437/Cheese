package ui;

import client.Exceptions.AlreadyTakenException;
import client.Exceptions.InvalidCredentialsException;
import client.ServerFacade;
import model.AuthData;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class PreLoginUI {

    private final ServerFacade facade;
    private final Scanner scanner;
    private AuthData authData;

    public PreLoginUI(ServerFacade facade, Scanner scanner) {
        this.facade = facade;
        this.scanner = scanner;
    }

    public void run() throws IOException, InterruptedException {
        boolean running = true;
        String commands = "Commands:\n" +
                "- help\n" +
                "- login\n" +
                "- register\n" +
                "- quit\n";
        System.out.println(commands);
        while (running) {
            System.out.print(">>> ");
            String input = scanner.nextLine();
            switch (input.toLowerCase(Locale.ROOT)) {
                case "help" -> System.out.println(commands);
                case "login" -> login();
                case "register" -> register();
                case "quit" -> running = false;
                default -> System.out.print("Invalid command\n");
            }
        }
    }

    public void register() throws IOException, InterruptedException {
        boolean registered = false;

        while (!registered) {
            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();
            try {
                this.authData = facade.register(username, password, email);
                registered = true;
            } catch (AlreadyTakenException error) {
                System.out.println("Username already taken try again");
            } catch (IOException | InterruptedException error) {
                System.out.println("A problem occurred with the server try again");
            }
        }
    }

    public void login() throws IOException, InterruptedException {
        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            try {
                this.authData = facade.login(username, password);
                loggedIn = true;
            } catch (InvalidCredentialsException error) {
                System.out.println("Incorrect username or password try again");
            } catch (IOException | InterruptedException error) {
                System.out.println("Problem occurred with logging in try again");
            }
        }
        PostLoginUI postLoginUI = new PostLoginUI(this.facade, this.scanner, this.authData);
        postLoginUI.run();
    }
}
