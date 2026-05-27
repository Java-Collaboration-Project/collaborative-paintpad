package client.ui;

import client.draw.CanvasManager;
import client.draw.ToolManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class WhiteboardController {
    private final BorderPane root;
    private final CanvasManager canvasManager;

    public WhiteboardController() {
        root = new BorderPane();
        ToolManager toolManager = new ToolManager();

        // Setup Layers
        Canvas gridCanvas = new Canvas(1920, 1080); // NEW: Background grid layer
        Canvas mainCanvas = new Canvas(1920, 1080);
        Canvas previewCanvas = new Canvas(1920, 1080);
        Pane overlayPane = new Pane();
        overlayPane.setPickOnBounds(false);

        StackPane canvasHolder = new StackPane();
        // gridCanvas must go FIRST so it sits beneath the drawings
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

        // Pass gridCanvas to CanvasManager
        canvasManager = new CanvasManager(mainCanvas, previewCanvas, gridCanvas, overlayPane, toolManager);

        ToolbarController toolbarController = new ToolbarController(toolManager, canvasManager);
        MenuBarController menuBarController = new MenuBarController(canvasManager);
        SidebarController sidebarController = new SidebarController(canvasManager);

        VBox topContainer = new VBox();
        topContainer.getChildren().addAll(menuBarController.getMenuBar(), toolbarController.getToolbar());

        root.setTop(topContainer);
        root.setLeft(sidebarController.getSidebar());
        root.setCenter(scrollPane);
    }

    public BorderPane getRoot() { return root; }
    public CanvasManager getCanvasManager() { return canvasManager; }
}