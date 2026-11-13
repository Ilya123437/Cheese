package ui;

import client.Exceptions.AlreadyTakenException;
import client.Exceptions.InvalidCredentialsException;
import client.Exceptions.UnauthorizedException;
import client.ServerFacade;
import model.AuthData;

import java.io.IOException;
import java.util.Scanner;

public class PreLoginUI {

    private final ServerFacade facade;
    private final Scanner scanner;
    private AuthData authData;

    public PreLoginUI(ServerFacade facade, Scanner scanner) {
        this.facade = facade;
        this.scanner = scanner;
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
    }
}
