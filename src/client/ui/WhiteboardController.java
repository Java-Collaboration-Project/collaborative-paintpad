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

    // For this example, we will use a default global session.
    // In a fully scaled app, this comes from the SessionSidebarController.
    private final String currentSessionId = "global-session";

    public WhiteboardController() {
        root = new BorderPane();
        ToolManager toolManager = new ToolManager();

        // 1. Setup UI Layers
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

        // Zoom Engine
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

        // 2. Initialize CanvasManager
        canvasManager = new CanvasManager(mainCanvas, previewCanvas, gridCanvas, overlayPane, toolManager);

        // 3. Initialize Networking (Resolving the dependency issue)
        NetworkDispatcher dispatcher = new NetworkDispatcher(canvasManager);
        connectionManager = new ConnectionManager();
        connectionManager.start("localhost", 5000, dispatcher);

        // 4. Wire CanvasManager to send outgoing drawing events to the server
        canvasManager.setOnDrawEventCreated(drawEvent -> {
            // Attach the current user and session metadata to the raw event
            drawEvent.sessionId = currentSessionId;
            drawEvent.userId = LoginController.currentUser != null ? LoginController.currentUser.getUsername() : "UnknownUser";

            // Wrap it in a network Message and send it
            Message msg = new Message(EventType.DRAW, drawEvent.sessionId, drawEvent.userId, drawEvent);
            connectionManager.getConnection().send(msg);
        });

        // 5. Send initial JOIN_SESSION message so the server syncs the late-joiner board state
        if (LoginController.currentUser != null) {
            Message joinMsg = new Message(EventType.JOIN_SESSION, currentSessionId, LoginController.currentUser.getUsername(), null);
            connectionManager.getConnection().send(joinMsg);
        }

        // 6. Setup remaining UI components
        ToolbarController toolbarController = new ToolbarController(toolManager, canvasManager);
        MenuBarController menuBarController = new MenuBarController(canvasManager);
        SidebarController sidebarController = new SidebarController(canvasManager);

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

//package client.ui;
//
//import client.draw.CanvasManager;
//import client.draw.ToolManager;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.StackPane;
//import javafx.scene.layout.VBox;
//
//public class WhiteboardController {
//    private final BorderPane root;
//    private final CanvasManager canvasManager;
//
//    public WhiteboardController() {
//        root = new BorderPane();
//        ToolManager toolManager = new ToolManager();
//
//        // Setup Layers
//        Canvas gridCanvas = new Canvas(1920, 1080); // NEW: Background grid layer
//        Canvas mainCanvas = new Canvas(1920, 1080);
//        Canvas previewCanvas = new Canvas(1920, 1080);
//        Pane overlayPane = new Pane();
//        overlayPane.setPickOnBounds(false);
//
//        StackPane canvasHolder = new StackPane();
//        // gridCanvas must go FIRST so it sits beneath the drawings
//        canvasHolder.getChildren().addAll(gridCanvas, mainCanvas, previewCanvas, overlayPane);
//        canvasHolder.setStyle("-fx-background-color: white;");
//
//        ScrollPane scrollPane = new ScrollPane();
//        scrollPane.setContent(canvasHolder);
//        scrollPane.setPannable(false);
//        scrollPane.setFitToWidth(true);
//        scrollPane.setFitToHeight(true);
//
//        // Zoom Engine
//        final double SCALE_DELTA = 1.1;
//        canvasHolder.setOnScroll(event -> {
//            if (event.isControlDown()) {
//                event.consume();
//                double zoomFactor = event.getDeltaY() > 0 ? SCALE_DELTA : 1 / SCALE_DELTA;
//                double currentScale = canvasHolder.getScaleX();
//                double newScale = currentScale * zoomFactor;
//                if (newScale >= 0.1 && newScale <= 5.0) {
//                    canvasHolder.setScaleX(newScale);
//                    canvasHolder.setScaleY(newScale);
//                }
//            }
//        });
//
//        // Pass gridCanvas to CanvasManager
//        canvasManager = new CanvasManager(mainCanvas, previewCanvas, gridCanvas, overlayPane, toolManager);
//
//        ToolbarController toolbarController = new ToolbarController(toolManager, canvasManager);
//        MenuBarController menuBarController = new MenuBarController(canvasManager);
//        SidebarController sidebarController = new SidebarController(canvasManager);
//
//        VBox topContainer = new VBox();
//        topContainer.getChildren().addAll(menuBarController.getMenuBar(), toolbarController.getToolbar());
//
//        root.setTop(topContainer);
//        root.setLeft(sidebarController.getSidebar());
//        root.setCenter(scrollPane);
//    }
//
//    public BorderPane getRoot() { return root; }
//    public CanvasManager getCanvasManager() { return canvasManager; }
//}