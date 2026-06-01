package server.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerCore {
    private final int port;
    private final ExecutorService threadPool;
    private volatile boolean running = true;

    public ServerCore(int port) {
        this.port = port;
        this.threadPool = Executors.newCachedThreadPool(); // Handles dynamic client loads
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Whiteboard Server started on port " + port);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                threadPool.execute(handler);
            }
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }

    public void stop() {
        this.running = false;
    }
}

//package server.network;
//
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class ServerCore {
//    private final ExecutorService threadPool = Executors.newCachedThreadPool();
//    private boolean isRunning = true;
//
//    public void start(int port) {
//        try (ServerSocket serverSocket = new ServerSocket(port)) {
//            System.out.println("Whiteboard Server running on port " + port);
//
//            while (isRunning) {
//                Socket socket = serverSocket.accept();
//                ClientHandler handler = new ClientHandler(socket);
//                threadPool.execute(handler);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            threadPool.shutdown();
//        }
//    }
//}

//package server.network;
//
//import shared.protocol.Message;
//
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.*;
//
//public class ServerCore {
//
//    public void start(int port) {
//        try {
//            ServerSocket serverSocket = new ServerSocket(port);
//
//            System.out.println("Server started...");
//
//            while (true) {
//                Socket socket = serverSocket.accept();
//                new Thread(new ClientHandler(socket)).start();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}