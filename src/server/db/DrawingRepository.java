package server.db;

import shared.model.DrawEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DrawingRepository {

    private static final Map<String, List<DrawEvent>> sessionCache = new ConcurrentHashMap<>();

    private static final java.util.Set<String> persistedEventIds = ConcurrentHashMap.newKeySet();

    private static volatile boolean flushed = false;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[ShutdownHook] Flushing drawing cache to database...");
            flushAll();
            System.out.println("[ShutdownHook] Flush complete.");
        }));
    }


    public static synchronized void flushAll() {
        if (flushed) {
            System.out.println("[Cache] Already flushed, skipping.");
            return;
        }
        flushCacheToDatabase();
        flushed = true;
    }

    public void saveDrawEvent(DrawEvent event) {
        if (event.actionId == null || event.actionId.trim().isEmpty()) {
            event.actionId = java.util.UUID.randomUUID().toString();
        }

        sessionCache.computeIfAbsent(event.sessionId, k -> Collections.synchronizedList(new ArrayList<>()))
                    .add(event);

        System.out.println("[Cache] Stored event " + event.actionId + " for session " + event.sessionId);
    }

    public List<DrawEvent> getEventsBySession(String sessionId) {
        List<DrawEvent> cached = sessionCache.get(sessionId);
        if (cached != null) {
            System.out.println("[Cache] HIT for session " + sessionId + " (" + cached.size() + " events)");
            synchronized (cached) {
                return new ArrayList<>(cached);
            }
        }

        System.out.println("[Cache] MISS for session " + sessionId + ", loading from DB...");
        List<DrawEvent> dbEvents = loadEventsFromDatabase(sessionId);

        List<DrawEvent> syncList = Collections.synchronizedList(new ArrayList<>(dbEvents));
        sessionCache.put(sessionId, syncList);

        for (DrawEvent e : dbEvents) {
            persistedEventIds.add(e.actionId);
        }

        return new ArrayList<>(dbEvents);
    }

    private List<DrawEvent> loadEventsFromDatabase(String sessionId) {
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
            System.err.println("Failed to retrieve session events from DB: " + e.getMessage());
        }
        return events;
    }

    private static void flushCacheToDatabase() {
        String sql = """
            INSERT INTO draw_events (
                id, session_id, user_id, tool, color, stroke_width, x1, y1, x2, y2, timestamp
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        int totalFlushed = 0;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Use a transaction for atomicity

            for (Map.Entry<String, List<DrawEvent>> entry : sessionCache.entrySet()) {
                List<DrawEvent> events = entry.getValue();
                synchronized (events) {
                    for (DrawEvent event : events) {
                        // Skip events that were originally loaded from the DB
                        if (persistedEventIds.contains(event.actionId)) {
                            continue;
                        }

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

                        stmt.addBatch();
                        totalFlushed++;
                    }
                }
            }

            stmt.executeBatch();
            conn.commit();
            System.out.println("[Flush] Successfully flushed " + totalFlushed + " new events to database.");

        } catch (Exception e) {
            System.err.println("[Flush] FAILED to flush cache to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}