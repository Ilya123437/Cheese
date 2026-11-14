package client;

import chess.*;
import ui.PreLoginUI;
import java.io.IOException;
import java.util.Scanner;

public class ClientMain {

    private ServerFacade facade;
    private Scanner scanner;

    public void main(String[] args) throws IOException, InterruptedException {
        this.scanner = new Scanner(System.in);
        this.facade = new ServerFacade(8080); // need to figure out how to get the actual port number
        System.out.println("♕ 240 Chess Client: ");
        PreLoginUI preLoginUI = new PreLoginUI(facade, scanner);
        preLoginUI.run();
    }
}
