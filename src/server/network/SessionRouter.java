package server.network;

import shared.protocol.Message;
import server.service.DrawService;
import server.service.WhiteboardStateService;
import server.db.DrawingRepository;
import shared.model.DrawEvent;
import client.util.JsonUtil;

public class SessionRouter {
    private static final DrawService drawService = new DrawService(new DrawingRepository());
    private static final WhiteboardStateService stateService = new WhiteboardStateService(new DrawingRepository());

    public static void route(Message message, ClientHandler sender) {
        switch (message.getType()) {
            case LOGIN:
                sender.setCurrentUserId(message.getSenderId());
                ConnectionRegistry.add(message.getSenderId(), sender);
                BroadcastManager.broadcast(new Message(shared.protocol.EventType.ACTIVE_USERS_LIST, "global-session", "SERVER", ConnectionRegistry.getActiveUsers()));
                break;

            case JOIN_SESSION:
                sender.setCurrentSessionId(message.getSessionId());
                stateService.syncBoardToClient(message.getSessionId(), message.getSenderId());
                break;

            case DRAW:
                String drawJson = JsonUtil.toJson(message.getPayload());
                DrawEvent event = JsonUtil.fromJson(drawJson, DrawEvent.class);

                drawService.processDrawEvent(event);

                BroadcastManager.broadcastToSession(message.getSessionId(), message);
                break;

            case REPLAY_BOARD:
                stateService.replaySession(message.getSessionId());
                break;

            case CLEAR_BOARD:
                BroadcastManager.broadcastToSession(message.getSessionId(), message);
                break;

            default:
                System.out.println("Unhandled event type: " + message.getType());
        }
    }
}