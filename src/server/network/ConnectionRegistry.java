package server.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionRegistry {
    // Thread-safe map for O(1) lookups of connected users
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

//package server.network;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class ConnectionRegistry {
//
//    private static Map<String, ClientHandler> users = new HashMap<>();
//
//    public static void add(String userId, ClientHandler handler) {
//        users.put(userId, handler);
//    }
//
//    public static ClientHandler get(String userId) {
//        return users.get(userId);
//    }
//
//    public static void remove(String userId) {
//    }
//}