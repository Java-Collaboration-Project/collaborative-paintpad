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