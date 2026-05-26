package server.service;

import server.db.DrawingRepository;
import shared.model.DrawEvent;

import java.util.Comparator;
import java.util.List;
public class WhiteboardStateService {

    private final DrawingRepository drawingRepository;

    public WhiteboardStateService(DrawingRepository drawingRepository) {
        this.drawingRepository = drawingRepository;
    }

    public List<DrawEvent> rebuildBoardState(String sessionId) {
        return drawingRepository.getEventsBySession(sessionId);
    }

    public void syncBoardToClient(String sessionId) {

        List<DrawEvent> events = rebuildBoardState(sessionId);

        for (DrawEvent event : events) {
            // Moss T will broadcast this
            System.out.println("Sync event: " + event.toolType);
        }
    }
    public void replaySession(String sessionId) {

        List<DrawEvent> events = rebuildBoardState(sessionId);

        events.sort(Comparator.comparingLong(e -> e.timestamp));

        for (DrawEvent event : events) {
            try {
                Thread.sleep(50); // animation effect
                // send to client step by step
            } catch (Exception ignored) {}
        }
    }
}