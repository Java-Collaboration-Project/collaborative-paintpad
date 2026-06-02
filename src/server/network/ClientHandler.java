package server.network;

import com.google.gson.Gson;
import shared.protocol.Message;
import client.util.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private String currentUserId;
    private String currentSessionId;
    private volatile boolean running = true;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out = new PrintWriter(socket.getOutputStream(), true);
            BroadcastManager.addClient(this);

            String line;
            while (running && (line = in.readLine()) != null) {
                Message message = JsonUtil.fromJson(line, Message.class);
                SessionRouter.route(message, this);
            }
        } catch (IOException e) {
            System.err.println("Client disconnected unexpectedly: " + currentUserId);
        } finally {
            cleanup();
        }
    }

    public void send(Message message) {
        if (out != null && !socket.isClosed()) {
            out.println(JsonUtil.toJson(message));
        }
    }

    public String getCurrentSessionId() {
        return currentSessionId;
    }

    public void setCurrentSessionId(String sessionId) {
        this.currentSessionId = sessionId;
    }

    public void setCurrentUserId(String userId) {
        this.currentUserId = userId;
    }

    private void cleanup() {
        running = false;
        BroadcastManager.removeClient(this);
        if (currentUserId != null) {
            ConnectionRegistry.remove(currentUserId);
            BroadcastManager.broadcast(new Message(shared.protocol.EventType.ACTIVE_USERS_LIST, "global-session", "SERVER", ConnectionRegistry.getActiveUsers()));
        }
        try {
            if (!socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}