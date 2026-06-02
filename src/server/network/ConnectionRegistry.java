package server.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionRegistry {
    private static final Map<String, ClientHandler> activeUsers = new ConcurrentHashMap<>();

    public static void add(String userId, ClientHandler handler) {
        activeUsers.put(userId, handler);
        System.out.println("User connected: " + userId + ". Total online: " + activeUsers.size());
    }

    public static void remove(String userId) {
        activeUsers.remove(userId);
        System.out.println("User disconnected: " + userId + ". Total online: " + activeUsers.size());
    }

    public static ClientHandler get(String userId) {
        return activeUsers.get(userId);
    }

    public static java.util.List<String> getActiveUsers() {
        return new java.util.ArrayList<>(activeUsers.keySet());
    }
}