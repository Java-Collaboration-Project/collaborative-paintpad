package client.network;

public class ConnectionManager {

    private final ClientConnection connection = new ClientConnection();

    public void start(String host, int port, NetworkDispatcher dispatcher) {
        try {
            connection.connect(host, port, dispatcher);
            System.out.println("Connected to server at " + host + ":" + port);
        } catch (Exception e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ClientConnection getConnection() {
        return connection;
    }
}

//package client.network;
//
//import client.draw.CanvasManager;
//import shared.protocol.Message;
//
//public class ConnectionManager {
//
//    private ClientConnection connection = new ClientConnection();
//
//    public void start() {
//        try {
//            connection.connect("localhost", 5000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public ClientConnection getConnection() {
//        return connection;
//    }
//}
//public class ConnectionManager {
//
//    private ClientConnection connection;
//
//    public void start(String host, int port, NetworkDispatcher dispatcher) throws Exception {
//        connection = new ClientConnection();
//        connection.connect(host, port);
//
//        MessageListener listener = new MessageListener(connection.getReader(), dispatcher);
//        new Thread(listener).start();
//    }
//
//    public void send(Message msg) {
//        connection.sendMessage(msg);
//    }
//}