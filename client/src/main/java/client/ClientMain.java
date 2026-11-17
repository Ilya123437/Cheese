package client;

import chess.*;
import ui.PreLoginUI;
import java.io.IOException;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        client.ServerFacade facade = new ServerFacade();
        System.out.println("♕ 240 Chess Client: ");
        PreLoginUI preLoginUI = new PreLoginUI(facade, scanner);
        preLoginUI.run();
    }
}
