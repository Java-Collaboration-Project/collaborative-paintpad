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
            INSERT INTO draw_events (
                id, session_id, user_id, tool_type, color, stroke_width, x1, y1, x2, y2, timestamp
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        if (event.actionId == null || event.actionId.trim().isEmpty()) {
            event.actionId = java.util.UUID.randomUUID().toString();
        }

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.actionId);
            stmt.setString(2, event.sessionId);
            stmt.setString(3, event.userId);
            stmt.setString(4, event.toolType);
            stmt.setString(5, event.color);
            stmt.setDouble(6, event.strokeWidth);
            stmt.setDouble(7, event.x1);
            stmt.setDouble(8, event.y1);
            stmt.setDouble(9, event.x2);
            stmt.setDouble(10, event.y2);
            stmt.setLong(11, event.timestamp);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Failed to save draw event: " + e.getMessage());
        }
    }

    public List<DrawEvent> getEventsBySession(String sessionId) {
        List<DrawEvent> events = new ArrayList<>();
        String sql = "SELECT * FROM draw_events WHERE session_id = ? ORDER BY timestamp ASC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sessionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DrawEvent event = new DrawEvent();
                    event.actionId = rs.getString("id");
                    event.sessionId = rs.getString("session_id");
                    event.userId = rs.getString("user_id");
                    event.toolType = rs.getString("tool_type");
                    event.color = rs.getString("color");
                    event.strokeWidth = rs.getDouble("stroke_width");
                    event.x1 = rs.getDouble("x1");
                    event.y1 = rs.getDouble("y1");
                    event.x2 = rs.getDouble("x2");
                    event.y2 = rs.getDouble("y2");
                    event.timestamp = rs.getLong("timestamp");
                    events.add(event);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to retrieve session events: " + e.getMessage());
        }
        return events;
    }
}

//package server.db;
//
//import shared.model.DrawEvent;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class DrawingRepository {
//
//    public void saveDrawEvent(DrawEvent event) {
//        String sql = """
//            INSERT INTO draw_events (
//                id, session_id, user_id, tool, color, stroke_width, x1, y1, x2, y2, timestamp
//            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
//        """;
//
//        try (Connection connection = DatabaseManager.getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//
//            statement.setString(1, event.getActionId() != null ? event.getActionId() : UUID.randomUUID().toString());
//            statement.setString(2, event.getSessionId() != null ? event.getSessionId() : "global-session");
//            statement.setString(3, event.getUserId());
//            statement.setString(4, event.getToolType());
//            statement.setString(5, event.getColor());
//            statement.setDouble(6, event.getStrokeWidth());
//            statement.setDouble(7, event.getX1());
//            statement.setDouble(8, event.getY1());
//            statement.setDouble(9, event.getX2());
//            statement.setDouble(10, event.getY2());
//            statement.setLong(11, event.getTimestamp());
//
//            statement.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public List<DrawEvent> getEventsBySession(String sessionId) {
//        List<DrawEvent> events = new ArrayList<>();
//        String sql = "SELECT * FROM draw_events WHERE session_id = ? ORDER BY timestamp";
//
//        try (Connection connection = DatabaseManager.getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//
//            statement.setString(1, sessionId);
//            ResultSet rs = statement.executeQuery();
//
//            while (rs.next()) {
//                DrawEvent event = new DrawEvent(
//                        rs.getDouble("x1"), rs.getDouble("y1"),
//                        rs.getDouble("x2"), rs.getDouble("y2"),
//                        rs.getString("color"), rs.getDouble("stroke_width"),
//                        rs.getString("tool")
//                );
//                event.setActionId(rs.getString("id"));
//                event.setSessionId(rs.getString("session_id"));
//                event.setUserId(rs.getString("user_id"));
//                event.setTimestamp(rs.getLong("timestamp"));
//
//                events.add(event);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return events;
//    }
//}

//package server.db;
//
//import shared.model.DrawEvent;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//
//public class DrawingRepository {
//
//    public void saveDrawEvent(DrawEvent event) {
//
//        String sql = """
//                    INSERT INTO draw_events(id, session_id, user_id, tool, color, stroke_width, x1, y1, x2, y2, timestamp
//                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
//                """;
//
//        try (Connection connection = DatabaseManager.getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//
//            statement.setString(1, event.getActionId());
//            statement.setString(2, event.getSessionId());
//            statement.setString(3, event.getUserId());
//            statement.setString(4, event.getTool().name());
//            statement.setString(5, event.getColor());
//            statement.setDouble(6, event.getStrokeWidth());
//            statement.setDouble(7, event.getX1());
//            statement.setDouble(8, event.getY1());
//            statement.setDouble(9, event.getX2());
//            statement.setDouble(10, event.getY2());
//            statement.setLong(11, event.getTimestamp());
//
//            statement.executeUpdate();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public List<DrawEvent> getEventsBySession(String sessionId) {
//
//        List<DrawEvent> events = new ArrayList<>();
//
//        String sql = "SELECT * FROM draw_events WHERE session_id = ? ORDER BY timestamp";
//
//        try (Connection connection = DatabaseManager.getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//
//            statement.setString(1, sessionId);
//
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//
////                DrawEvent event = new DrawEvent(0.0,0.0,0.0,0.0,"color",0.0,"pen");
//                DrawEvent event = new DrawEvent();
//
//                event.setActionId(resultSet.getString("id"));
//                event.setSessionId(resultSet.getString("session_id"));
//                event.setUserId(resultSet.getString("user_id"));
//                event.setColor(resultSet.getString("color"));
//                event.setStrokeWidth(resultSet.getDouble("stroke_width"));
//                event.setX1(resultSet.getDouble("x1"));
//                event.setY1(resultSet.getDouble("y1"));
//                event.setX2(resultSet.getDouble("x2"));
//                event.setY2(resultSet.getDouble("y2"));
//                event.setTimestamp(resultSet.getLong("timestamp"));
//
//                events.add(event);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return events;
//    }
////    public List<DrawEvent> getEventsBySession(String sessionId) {
////        return new ArrayList<>();
////    }
//}