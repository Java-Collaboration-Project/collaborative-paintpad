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

    private final Map<String, Image> sessionDrafts;
    private String currentUser = null;
    private final java.util.function.Consumer<String> onSessionChanged;

    public SidebarController(CanvasManager canvasManager, java.util.function.Consumer<String> onSessionChanged) {
        this.canvasManager = canvasManager;
        this.onSessionChanged = onSessionChanged;
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
                "Team Sync", "Personal Draft"
        );
        userList.setItems(users);

        currentUser = users.get(0);
        userList.getSelectionModel().select(0);

        userList.getSelectionModel().selectedItemProperty().addListener((obs, oldUser, newUser) -> {
            if (newUser != null && !newUser.equals(currentUser)) {

                if (currentUser != null) {
                    sessionDrafts.put(currentUser, canvasManager.getCanvasSnapshot());
                }

                Image savedDraft = sessionDrafts.get(newUser);

                canvasManager.clearHistory();

                canvasManager.loadCanvasSnapshot(savedDraft);

                currentUser = newUser;
                System.out.println("Switched session to: " + newUser);

                String newSessionId = "global-session";
                if (newUser.equals("Team Sync")) {
                    newSessionId = "global-session";
                } else if (newUser.equals("Personal Draft")) {
                    String myUsername = client.ui.LoginController.currentUser != null ? client.ui.LoginController.currentUser.getUsername() : "unknown";
                    newSessionId = "personal-" + myUsername;
                } else {
                    String myUsername = client.ui.LoginController.currentUser != null ? client.ui.LoginController.currentUser.getUsername() : "unknown";
                    if (myUsername.compareTo(newUser) < 0) {
                        newSessionId = "dm-" + myUsername + "-" + newUser;
                    } else {
                        newSessionId = "dm-" + newUser + "-" + myUsername;
                    }
                }
                if (onSessionChanged != null) {
                    onSessionChanged.accept(newSessionId);
                }
            }
        });

        sidebar.getChildren().addAll(header, userList);
    }

    public void updateActiveUsers(java.util.List<String> onlineUsers) {
        ObservableList<String> users = FXCollections.observableArrayList();
        users.add("Team Sync");
        users.add("Personal Draft");
        
        String myUsername = client.ui.LoginController.currentUser != null ? client.ui.LoginController.currentUser.getUsername() : "";
        
        for (String user : onlineUsers) {
            if (!user.equals(myUsername)) {
                users.add(user);
            }
        }
        
        String currentSelection = userList.getSelectionModel().getSelectedItem();
        userList.setItems(users);
        if (currentSelection != null && users.contains(currentSelection)) {
            userList.getSelectionModel().select(currentSelection);
        } else {
            userList.getSelectionModel().select("Team Sync");
        }
    }
}