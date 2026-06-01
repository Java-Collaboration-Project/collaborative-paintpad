package client.network;

import shared.protocol.Message;
import client.util.JsonUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {
    private Socket socket;
    private PrintWriter out;
    private MessageListener listener;

    public void connect(String host, int port, NetworkDispatcher dispatcher) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);

        listener = new MessageListener(socket, dispatcher);
        Thread listenerThread = new Thread(listener);
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void send(Message message) {
        if (out != null && !socket.isClosed()) {
            String json = JsonUtil.toJson(message);
            out.println(json);
        }
    }

    public void close() throws IOException {
        if (listener != null) listener.stop();
        if (socket != null && !socket.isClosed()) socket.close();
    }
}

//package client.network;
//
//import shared.protocol.Message;
//import client.util.JsonUtil;
//
//import java.io.*;
//import java.net.Socket;
//
//public class ClientConnection {
//
//    private Socket socket;
//    private PrintWriter out;
//
//    public void connect(String host, int port) throws IOException {
//        socket = new Socket(host, port);
//        out = new PrintWriter(socket.getOutputStream(), true);
//
//        new Thread(new MessageListener(socket)).start();
//    }
//
//    public void send(Message message) {
//        String json = JsonUtil.toJson(message);
//        out.println(json);
//    }
//
//    public void close() throws IOException {
//        socket.close();
//    }
//}

//import client.util.JsonUtil;
//import shared.protocol.Message;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//public class ClientConnection {
//
//    private Socket socket;
//    private PrintWriter out;
//    private BufferedReader in;
//
//    public void connect(String host, int port) throws Exception {
//        socket = new Socket(host, port);
//        out = new PrintWriter(socket.getOutputStream(), true);
//        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//    }
//
//    public void sendMessage(Message msg) {
//        String json = JsonUtil.toJson(msg);
//        out.println(json);
//    }
//
//    public BufferedReader getReader() {
//        return in;
//    }
//
//    public void close() throws Exception {
//        socket.close();
//    }
//}