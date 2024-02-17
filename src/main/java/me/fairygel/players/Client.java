package me.fairygel.players;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Server {
    private final Socket socket;
    private final PrintWriter output;
    private String name;

    public Client(String addr, int port) {
        try {
            socket = new Socket(addr, port);
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void sendChar() {
        output.println(getChar());
    }

    public void sendDirection() {
        output.println(getDirection());
    }

    public void sendInt() {
        output.println(getInt());
    }

    public void sendName() {
        output.println(getString());
    }

}
