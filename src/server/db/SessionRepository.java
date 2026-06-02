package server.db;

import shared.model.WhiteboardSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SessionRepository {
    public void createSession(WhiteboardSession session) {
        String sql = "INSERT INTO sessions (id, name, type, owner_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, session.getSessionId());
            stmt.setString(2, session.getName());
            stmt.setString(3, session.getType());
            stmt.setString(4, session.getOwnerId());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error creating session: " + e.getMessage());
        }
    }

    public List<WhiteboardSession> getAllSessions() {
        List<WhiteboardSession> sessions = new ArrayList<>();
        String sql = "SELECT * FROM sessions";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                sessions.add(new WhiteboardSession(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("owner_id")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error fetching sessions: " + e.getMessage());
        }
        return sessions;
    }
}