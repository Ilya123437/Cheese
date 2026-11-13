package client;

import chess.*;
import ui.PreLoginUI;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        ServerFacade facade = new ServerFacade(8080);
        PreLoginUI preLoginUI = new PreLoginUI(facade, scanner);
        System.out.println("♕ 240 Chess Client: ");
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
                case "login" -> preLoginUI.login();
                case "register" -> preLoginUI.register();
                case "quit" -> running = false;
                default -> System.out.print("Invalid command\n");
            }
        }

    }
}
