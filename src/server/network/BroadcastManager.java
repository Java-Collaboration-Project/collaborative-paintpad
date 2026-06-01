package server.network;

import shared.protocol.Message;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BroadcastManager {
    // Thread-safe collection to prevent ConcurrentModificationException during active drawing
    private static final Set<ClientHandler> activeClients = ConcurrentHashMap.newKeySet();

    public static void addClient(ClientHandler client) {
        activeClients.add(client);
    }

    public static void removeClient(ClientHandler client) {
        activeClients.remove(client);
    }

    public static void broadcast(Message message) {
        for (ClientHandler client : activeClients) {
            client.send(message);
        }
    }

    public static void broadcastToSession(String sessionId, Message message) {
        for (ClientHandler client : activeClients) {
            if (sessionId.equals(client.getCurrentSessionId())) {
                client.send(message);
            }
        }
    }
}

//package server.network;
//
//import shared.protocol.Message;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class BroadcastManager {
//    private static final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();
//
//    public static void addClient(ClientHandler client) {
//        clients.add(client);
//    }
//
//    public static void removeClient(ClientHandler client) {
//        clients.remove(client);
//    }
//
//    public static void broadcast(Message message, ClientHandler excludeClient) {
//        for (ClientHandler client : clients) {
//            if (client != excludeClient) {
//                client.send(message);
//            }
//        }
//    }
//
//    public static void broadcastToAll(Message message) {
//        for (ClientHandler client : clients) {
//            client.send(message);
//        }
//    }
//}

//package server.network;
//
//import shared.protocol.Message;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BroadcastManager {
//
//    private static List<ClientHandler> clients = new ArrayList<>();
//
//    public static void addClient(ClientHandler client) {
//        clients.add(client);
//    }
//
//    public static void broadcast(Message message) {
//        for (ClientHandler client : clients) {
//            client.send(message);
//        }
//    }
//}