package server.service;

import server.db.DrawingRepository;
import shared.model.DrawEvent;
import shared.protocol.Message;
import shared.protocol.EventType;
import server.network.BroadcastManager;

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

    public void syncBoardToClient(String sessionId, String targetUserId) {
        List<DrawEvent> events = rebuildBoardState(sessionId);
        Message syncMessage = new Message(EventType.SYNC_BOARD, sessionId, "SERVER", events);
        server.network.ClientHandler target = server.network.ConnectionRegistry.get(targetUserId);
        if (target != null) {
            target.send(syncMessage);
        }
    }

    public void replaySession(String sessionId) {
        List<DrawEvent> events = rebuildBoardState(sessionId);
        events.sort(Comparator.comparingLong(e -> e.timestamp));

        new Thread(() -> {
            Message clearMsg = new Message(EventType.CLEAR_BOARD, sessionId, "SERVER", null);
            BroadcastManager.broadcastToSession(sessionId, clearMsg);

            for (DrawEvent event : events) {
                try {
                    Thread.sleep(50);
                    Message drawMsg = new Message(EventType.DRAW, sessionId, event.userId, event);
                    BroadcastManager.broadcastToSession(sessionId, drawMsg);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}