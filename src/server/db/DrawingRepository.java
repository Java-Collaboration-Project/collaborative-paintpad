package server.db;

import shared.model.DrawEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DrawingRepository {

    public void saveDrawEvent(DrawEvent event) {

        String sql = """
                    INSERT INTO draw_events(id, session_id, user_id, tool, color, stroke_width, x1, y1, x2, y2, timestamp
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, event.getActionId());
            statement.setString(2, event.getSessionId());
            statement.setString(3, event.getUserId());
            statement.setString(4, event.getTool().name());
            statement.setString(5, event.getColor());
            statement.setDouble(6, event.getStrokeWidth());
            statement.setDouble(7, event.getX1());
            statement.setDouble(8, event.getY1());
            statement.setDouble(9, event.getX2());
            statement.setDouble(10, event.getY2());
            statement.setLong(11, event.getTimestamp());

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<DrawEvent> getEventsBySession(String sessionId) {

        List<DrawEvent> events = new ArrayList<>();

        String sql = "SELECT * FROM draw_events WHERE session_id = ? ORDER BY timestamp";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, sessionId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

//                DrawEvent event = new DrawEvent(0.0,0.0,0.0,0.0,"color",0.0,"pen");
                DrawEvent event = new DrawEvent();

                event.setActionId(resultSet.getString("id"));
                event.setSessionId(resultSet.getString("session_id"));
                event.setUserId(resultSet.getString("user_id"));
                event.setColor(resultSet.getString("color"));
                event.setStrokeWidth(resultSet.getDouble("stroke_width"));
                event.setX1(resultSet.getDouble("x1"));
                event.setY1(resultSet.getDouble("y1"));
                event.setX2(resultSet.getDouble("x2"));
                event.setY2(resultSet.getDouble("y2"));
                event.setTimestamp(resultSet.getLong("timestamp"));

                events.add(event);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }
//    public List<DrawEvent> getEventsBySession(String sessionId) {
//        return new ArrayList<>();
//    }
}