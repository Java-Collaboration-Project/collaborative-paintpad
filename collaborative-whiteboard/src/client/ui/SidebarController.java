package client.ui;

import client.draw.CanvasManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class SidebarController {
    private final VBox sidebar;
    private final ListView<String> userList;
    private final CanvasManager canvasManager;

    // Data structure to hold session drafts locally in RAM
    private final Map<String, Image> sessionDrafts;
    private String currentUser = null; // Tracks who we are currently drawing with

    public SidebarController(CanvasManager canvasManager) {
        this.canvasManager = canvasManager;
        this.sidebar = new VBox(10);
        this.userList = new ListView<>();
        this.sessionDrafts = new HashMap<>();
        setupSidebar();
    }

    public VBox getSidebar() { return sidebar; }

    private void setupSidebar() {
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #fafafa; -fx-border-color: #cccccc; -fx-border-width: 0 1 0 0;");

        Label header = new Label("Active Sessions");
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        ObservableList<String> users = FXCollections.observableArrayList(
                "Moss F. (New)", "Moss T. (Existing)", "Team Sync", "Personal Draft"
        );
        userList.setItems(users);

        // Select the first user by default to start the session mapping
        currentUser = users.get(0);
        userList.getSelectionModel().select(0);

        // Session Switching Logic
        userList.getSelectionModel().selectedItemProperty().addListener((obs, oldUser, newUser) -> {
            if (newUser != null && !newUser.equals(currentUser)) {

                // 1. Save the current canvas state to the old user's map slot
                if (currentUser != null) {
                    sessionDrafts.put(currentUser, canvasManager.getCanvasSnapshot());
                }

                // 2. Load the new user's saved draft (will return null if it's their first time)
                Image savedDraft = sessionDrafts.get(newUser);

                // 3. Clear Undo history so you don't undo into another user's drawing
                canvasManager.clearHistory();

                // 4. Paint the saved draft (or a blank slate) onto the canvas
                canvasManager.loadCanvasSnapshot(savedDraft);

                // 5. Update the current user tracker
                currentUser = newUser;
                System.out.println("Switched session to: " + newUser);
            }
        });

        sidebar.getChildren().addAll(header, userList);
    }
}