package server.network;

import server.db.DatabaseInitializer;
import server.db.DrawingRepository;

public class ServerMain {
    public static void main(String[] args) {
        System.out.println("Initializing Database...");
        DatabaseInitializer.initialize();

        int port = 5000;
        ServerCore serverCore = new ServerCore(port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            DrawingRepository.flushAll();
            serverCore.stop();
        }));

        serverCore.start();
    }
}
