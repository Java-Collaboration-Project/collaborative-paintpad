package client.network;

import client.draw.CanvasManager;
import client.util.JsonUtil;
import shared.model.DrawEvent;
import shared.protocol.Message;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;

import java.util.List;

public class NetworkDispatcher {
    private final CanvasManager canvasManager;
    private java.util.function.Consumer<List<String>> onActiveUsersUpdated;

    public NetworkDispatcher(CanvasManager canvasManager) {
        this.canvasManager = canvasManager;
    }

    public void setOnActiveUsersUpdated(java.util.function.Consumer<List<String>> callback) {
        this.onActiveUsersUpdated = callback;
    }

    public void handle(Message message) {
        // All UI updates must happen on the JavaFX Application Thread
        Platform.runLater(() -> {
            switch (message.getType()) {
                case DRAW:
                    String drawJson = JsonUtil.toJson(message.getPayload());
                    DrawEvent event = JsonUtil.fromJson(drawJson, DrawEvent.class);
                    canvasManager.drawRemoteEvent(event);
                    break;

                case SYNC_BOARD:
                    String syncJson = JsonUtil.toJson(message.getPayload());
                    List<DrawEvent> events = JsonUtil.fromJson(syncJson, new TypeToken<List<DrawEvent>>(){}.getType());
                    canvasManager.clearCanvas();
                    for (DrawEvent e : events) {
                        canvasManager.drawRemoteEvent(e);
                    }
                    break;

                case CLEAR_BOARD:
                    canvasManager.clearCanvas();
                    break;

                case ACTIVE_USERS_LIST:
                    if (onActiveUsersUpdated != null) {
                        String userListJson = JsonUtil.toJson(message.getPayload());
                        List<String> activeUsers = JsonUtil.fromJson(userListJson, new TypeToken<List<String>>(){}.getType());
                        onActiveUsersUpdated.accept(activeUsers);
                    }
                    break;

                default:
                    System.out.println("Unhandled event type on client: " + message.getType());
                    break;
            }
        });
    }
}

//package client.network;
//
//import client.draw.CanvasManager;
//import client.util.JsonUtil;
//import javafx.application.Platform;
//import shared.model.DrawEvent;
//import shared.protocol.Message;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class NetworkDispatcher {
//    private static CanvasManager canvasManager;
//
//    public static void setCanvasManager(CanvasManager manager) {
//        canvasManager = manager;
//    }
//
//    public static void handle(Message message) {
//        Platform.runLater(() -> {
//            if (canvasManager == null) return;
//
//            switch (message.getType()) {
//                case DRAW:
//                    DrawEvent event = JsonUtil.fromJson(JsonUtil.toJson(message.getPayload()), DrawEvent.class);
//                    canvasManager.drawRemoteEvent(event);
//                    break;
//                case SYNC_BOARD:
//                    DrawEvent[] events = JsonUtil.fromJson(JsonUtil.toJson(message.getPayload()), DrawEvent[].class);
//                    canvasManager.clearCanvas();
//                    for (DrawEvent e : events) {
//                        canvasManager.drawRemoteEvent(e);
//                    }
//                    break;
//                case UNDO:
//                    canvasManager.undo();
//                    break;
//                case REDO:
//                    canvasManager.redo();
//                    break;
//                case CLEAR_BOARD:
//                    canvasManager.clearCanvas();
//                    break;
//                default:
//                    System.out.println("Unhandled event type: " + message.getType());
//            }
//        });
//    }
//}

//package client.network;
//
//import client.draw.CanvasManager;
//import client.util.JsonUtil;
//import shared.model.DrawEvent;
//import shared.protocol.Message;
//
//import shared.protocol.EventType;
//
//import static shared.protocol.EventType.*;
//
//public class NetworkDispatcher {
//
//    public static void handle(Message message) {
//
//        switch (message.getType()) {
//
//            case DRAW:
//                System.out.println("Draw event received");
//                // call CanvasManager.drawRemote(...)
//                break;
//
//            case  SYNC_BOARD:
//                System.out.println("Sync board");
//                break;
//
//            case UNDO:
//                System.out.println("Undo received");
//                break;
//
//            default:
//                System.out.println("Unknown event");
//        }
//    }
//}