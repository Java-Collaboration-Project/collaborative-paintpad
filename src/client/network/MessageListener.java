package client.network;

import client.util.JsonUtil;
import shared.protocol.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageListener implements Runnable {
    private final Socket socket;
    private final NetworkDispatcher dispatcher;
    private volatile boolean running = true;

    public MessageListener(Socket socket, NetworkDispatcher dispatcher) {
        this.socket = socket;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line;
            while (running && (line = in.readLine()) != null) {
                Message message = JsonUtil.fromJson(line, Message.class);
                dispatcher.handle(message);
            }
        } catch (IOException e) {
            if (running) System.err.println("Connection lost to server.");
        }
    }

    public void stop() {
        this.running = false;
    }
}