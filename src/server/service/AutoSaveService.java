package server.service;

import server.db.DrawingRepository;
import client.export.SaveManager;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoSaveService {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final SaveManager saveManager;
    private final String activeSessionId;

    public AutoSaveService(SaveManager saveManager, String sessionId) {
        this.saveManager = saveManager;
        this.activeSessionId = sessionId;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                String filePath = "autosave_" + activeSessionId + ".json";
                saveManager.saveSessionToFile(activeSessionId, filePath);
                System.out.println("Auto-saved session data to " + filePath);
            } catch (Exception e) {
                System.err.println("Auto-save failed: " + e.getMessage());
            }
        }, 30, 30, TimeUnit.SECONDS); // Every 30 seconds as requested
    }

    public void stop() {
        scheduler.shutdown();
    }
}