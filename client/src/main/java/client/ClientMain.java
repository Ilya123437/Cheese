package client;

import chess.*;
import ui.PreLoginUI;
import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("♕ 240 Chess Client: ");
        PreLoginUI preLoginUI = new PreLoginUI();
        preLoginUI.run();
    }
}
