package server.network;

import shared.protocol.Message;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BroadcastManager {
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