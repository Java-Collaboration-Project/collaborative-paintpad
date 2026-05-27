package server.db;

import client.export.SaveManager;
import shared.model.WhiteboardAction;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ActionRepository {

    public void saveAction(WhiteboardAction action) {

        String sql = """
                INSERT INTO actions(
                    id,
                    session_id,
                    action_type,
                    draw_event_id,
                    timestamp
                ) VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, action.getActionId());
            statement.setString(2, action.getDrawEvent().getSessionId());
            statement.setString(3, action.getActionType().name());
            statement.setString(4, action.getDrawEvent().getActionId());
            statement.setLong(5, action.getTimestamp());

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class AutoSaveService {

        private final SaveManager saveManager;

        public AutoSaveService(SaveManager saveManager) {
            this.saveManager = saveManager;
        }

        public void startAutoSave(String sessionId) {

            new Thread(() -> {

                while (true) {

                    try {
                        Thread.sleep(30000); // 30 seconds

                        saveManager.saveSessionToFile(
                                sessionId,
                                "autosave_" + sessionId + ".json"
                        );

                        System.out.println("Auto-saved session");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }).start();
        }
    }
}