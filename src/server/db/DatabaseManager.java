package server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

   // private static final String URL = "jdbc:mysql://localhost:3306/whiteboard_db"; //temporary url since i didn't create the db
    private static final String URL = "jdbc:postgresql://localhost:5432/collabWhiteboard";
    private static final String USER = "root";
    private static final String PASSWORD = "m0ss13";

    static {
        try {
            Class.forName("org.postgresql.Driver"); // org.posql or sth
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}