package client.network;

import client.draw.CanvasManager;
import shared.model.DrawEvent;

import java.util.List;

public class SessionSyncManager {

    public void syncBoard() {
        System.out.println("Syncing board...");
    }
    public void handleSync(List<DrawEvent> events, CanvasManager canvas) {
        for (DrawEvent e : events) {
//            canvas.drawRemote(e);
            canvas.drawRemoteEvent(e);
        }
    }
}