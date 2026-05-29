package server.db;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id VARCHAR(50) PRIMARY KEY,
                    username VARCHAR(100),
                    online BOOLEAN,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS sessions (
                    id VARCHAR(50) PRIMARY KEY,
                    name VARCHAR(100),
                    type VARCHAR(20),
                    owner_id VARCHAR(50),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS draw_events (
                    id VARCHAR(50) PRIMARY KEY,
                    session_id VARCHAR(50),
                    user_id VARCHAR(50),
                    tool VARCHAR(50),
                    color VARCHAR(20),
                    stroke_width double precision,
                    x1 double precision,
                    y1 double precision,
                    x2 double precision,
                    y2 double precision,
                    timestamp BIGINT
                )
            """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS actions (
                    id VARCHAR(50) PRIMARY KEY,
                    session_id VARCHAR(50),
                    action_type VARCHAR(50),
                    draw_event_id VARCHAR(50),
                    timestamp BIGINT
                )
            """);

            System.out.println("Database initialized successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}