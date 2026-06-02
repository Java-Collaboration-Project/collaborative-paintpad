package client.ui;

import client.draw.CanvasManager;
import client.draw.ToolManager;
import client.network.ConnectionManager;
import client.network.NetworkDispatcher;
import shared.protocol.EventType;
import shared.protocol.Message;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class WhiteboardController {
    private final BorderPane root;
    private final CanvasManager canvasManager;
    private final ConnectionManager connectionManager;

    private String currentSessionId = "global-session";

    public void setCurrentSessionId(String newSessionId) {
        if (!this.currentSessionId.equals(newSessionId)) {
            this.currentSessionId = newSessionId;
            if (LoginController.currentUser != null) {
                Message joinMsg = new Message(EventType.JOIN_SESSION, currentSessionId, LoginController.currentUser.getUsername(), null);
                connectionManager.getConnection().send(joinMsg);
            }
        }
    }

    public WhiteboardController() {
        root = new BorderPane();
        ToolManager toolManager = new ToolManager();

        Canvas gridCanvas = new Canvas(1920, 1080);
        Canvas mainCanvas = new Canvas(1920, 1080);
        Canvas previewCanvas = new Canvas(1920, 1080);
        Pane overlayPane = new Pane();
        overlayPane.setPickOnBounds(false);

        StackPane canvasHolder = new StackPane();
        canvasHolder.getChildren().addAll(gridCanvas, mainCanvas, previewCanvas, overlayPane);
        canvasHolder.setStyle("-fx-background-color: white;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(canvasHolder);
        scrollPane.setPannable(false);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        final double SCALE_DELTA = 1.1;
        canvasHolder.setOnScroll(event -> {
            if (event.isControlDown()) {
                event.consume();
                double zoomFactor = event.getDeltaY() > 0 ? SCALE_DELTA : 1 / SCALE_DELTA;
                double currentScale = canvasHolder.getScaleX();
                double newScale = currentScale * zoomFactor;

                if (newScale >= 0.1 && newScale <= 5.0) {
                    canvasHolder.setScaleX(newScale);
                    canvasHolder.setScaleY(newScale);
                }
            }
        });

        canvasManager = new CanvasManager(mainCanvas, previewCanvas, gridCanvas, overlayPane, toolManager);

        NetworkDispatcher dispatcher = new NetworkDispatcher(canvasManager);
        connectionManager = new ConnectionManager();
        connectionManager.start("localhost", 5000, dispatcher);

        canvasManager.setOnDrawEventCreated(drawEvent -> {
            drawEvent.sessionId = currentSessionId;
            drawEvent.userId = LoginController.currentUser != null ? LoginController.currentUser.getUsername() : "UnknownUser";

            Message msg = new Message(EventType.DRAW, drawEvent.sessionId, drawEvent.userId, drawEvent);
            connectionManager.getConnection().send(msg);
        });

        if (LoginController.currentUser != null) {
            Message loginMsg = new Message(EventType.LOGIN, currentSessionId, LoginController.currentUser.getUsername(), null);
            connectionManager.getConnection().send(loginMsg);

            Message joinMsg = new Message(EventType.JOIN_SESSION, currentSessionId, LoginController.currentUser.getUsername(), null);
            connectionManager.getConnection().send(joinMsg);
        }

        ToolbarController toolbarController = new ToolbarController(toolManager, canvasManager);
        MenuBarController menuBarController = new MenuBarController(canvasManager);
        SidebarController sidebarController = new SidebarController(canvasManager, this::setCurrentSessionId);
        dispatcher.setOnActiveUsersUpdated(sidebarController::updateActiveUsers);

        VBox topContainer = new VBox();
        topContainer.getChildren().addAll(menuBarController.getMenuBar(), toolbarController.getToolbar());

        root.setTop(topContainer);
        root.setLeft(sidebarController.getSidebar());
        root.setCenter(scrollPane);
    }

    public BorderPane getRoot() {
        return root;
    }

    public CanvasManager getCanvasManager() {
        return canvasManager;
    }
}