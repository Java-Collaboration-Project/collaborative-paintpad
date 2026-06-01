package client.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shared.model.User;

public class LoginController {
    private final Stage stage;
    public static User currentUser;

    public LoginController(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f4f4f4;");

        Label titleLabel = new Label("Collaborative Whiteboard");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setMaxWidth(250);

        Button loginBtn = new Button("Join Whiteboard");
        loginBtn.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-weight: bold;");
        loginBtn.setMaxWidth(250);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            if (username.isEmpty()) {
                errorLabel.setText("Username cannot be empty.");
            } else {
                currentUser = new User(java.util.UUID.randomUUID().toString(), username);
                launchWhiteboard();
            }
        });

        root.getChildren().addAll(titleLabel, usernameField, loginBtn, errorLabel);
        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    private void launchWhiteboard() {
        WhiteboardController whiteboardController = new WhiteboardController();
        Scene scene = new Scene(whiteboardController.getRoot(), 1200, 800);
        stage.setTitle("Whiteboard - " + currentUser.getUsername());
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}

//package client.ui;
//
//import client.network.ConnectionManager;
//import client.network.NetworkDispatcher;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import shared.model.User;
//import shared.protocol.EventType;
//import shared.protocol.Message;
//
//public class LoginController {
//    private final Stage stage;
//    private final ConnectionManager connectionManager;
//    public static User loggedInUser;
//
//    public LoginController(Stage stage, ConnectionManager connectionManager) {
//        this.stage = stage;
//        this.connectionManager = connectionManager;
//    }
//
//    public void show() {
//        VBox root = new VBox(15);
//        root.setAlignment(Pos.CENTER);
//        root.setPadding(new Insets(20));
//
//        Label title = new Label("Collaborative Whiteboard Login");
//        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
//
//        TextField usernameInput = new TextField();
//        usernameInput.setPromptText("Enter Username");
//        usernameInput.setMaxWidth(250);
//
//        Button loginBtn = new Button("Join Session");
//        loginBtn.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-weight: bold;");
//
//        loginBtn.setOnAction(e -> {
//            String username = usernameInput.getText().trim();
//            if (!username.isEmpty()) {
//                loggedInUser = new User(username, username);
//                connectionManager.start();
//
//                Message loginMsg = new Message(EventType.LOGIN, "global-session", username, null);
//                connectionManager.getConnection().send(loginMsg);
//
//                WhiteboardController whiteboard = new WhiteboardController(connectionManager);
//                NetworkDispatcher.setCanvasManager(whiteboard.getCanvasManager());
//
//                Scene scene = new Scene(whiteboard.getRoot(), 1200, 800);
//                stage.setScene(scene);
//                stage.setTitle("Whiteboard - " + username);
//                stage.centerOnScreen();
//            }
//        });
//
//        root.getChildren().addAll(title, usernameInput, loginBtn);
//        stage.setScene(new Scene(root, 400, 300));
//        stage.setTitle("Login");
//        stage.show();
//    }
//}