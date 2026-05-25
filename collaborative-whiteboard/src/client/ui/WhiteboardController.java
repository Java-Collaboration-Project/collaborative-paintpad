// client/ui/WhiteboardController.java
package client.ui;

import client.draw.CanvasManager;
import client.draw.ToolManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class WhiteboardController {
    private final BorderPane root;
    private final CanvasManager canvasManager;

    public WhiteboardController() {
        root = new BorderPane();
        ToolManager toolManager = new ToolManager();

        Canvas mainCanvas = new Canvas(1920, 1080);
        Canvas previewCanvas = new Canvas(1920, 1080);

        // NEW: Transparent overlay node layer to anchor native text pads natively
        Pane overlayPane = new Pane();
        overlayPane.setPickOnBounds(false); // Allows clicks on empty spaces to pass through to canvas

        StackPane canvasHolder = new StackPane();
        canvasHolder.getChildren().addAll(mainCanvas, previewCanvas, overlayPane);
        canvasHolder.setStyle("-fx-background-color: white;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(canvasHolder);
        scrollPane.setPannable(false);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Pass the overlay pane into your CanvasManager workspace
        canvasManager = new CanvasManager(mainCanvas, previewCanvas, overlayPane, toolManager);
        ToolbarController toolbarController = new ToolbarController(toolManager, canvasManager);

        root.setTop(toolbarController.getToolbar());
        root.setCenter(scrollPane);
    }

    public BorderPane getRoot() { return root; }
    public CanvasManager getCanvasManager() { return canvasManager; }
}