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

//    public void close() throws IOException {
//        if (listener != null) listener.stop();
//        if (socket != null && !socket.isClosed()) socket.close();
//    }
}