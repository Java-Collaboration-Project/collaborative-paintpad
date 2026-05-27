package client.draw;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import shared.DrawEvent;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import java.io.File;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import java.util.Stack;
import javafx.scene.control.Label;
import java.util.HashMap;
import java.util.Map;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public class CanvasManager {
    private final Stack<Image> undoHistory = new Stack<>();
    private final Stack<Image> redoHistory = new Stack<>();
    private final int MAX_HISTORY = 15;

    private final Canvas mainCanvas;
    private final Canvas previewCanvas;
    private final Canvas gridCanvas;
    private final Pane overlayPane;
    private final GraphicsContext mainGc;
    private final GraphicsContext previewGc;
    private final ToolManager toolManager;
    private Consumer<DrawEvent> onDrawEventCreated;

    private double startX, startY;

    private static class Point {
        int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }
    }

    private final Map<String, Label> remoteCursors = new HashMap<>();

    public CanvasManager(Canvas mainCanvas, Canvas previewCanvas, Canvas gridCanvas, Pane overlayPane, ToolManager toolManager) {
        this.mainCanvas = mainCanvas;
        this.previewCanvas = previewCanvas;
        this.gridCanvas = gridCanvas;
        this.overlayPane = overlayPane;
        this.mainGc = mainCanvas.getGraphicsContext2D();
        this.previewGc = previewCanvas.getGraphicsContext2D();
        this.toolManager = toolManager;

        drawGridPattern(); // Initialize the grid
        setupMouseEvents();
    }

    public void setOnDrawEventCreated(Consumer<DrawEvent> listener) {
        this.onDrawEventCreated = listener;
    }

    private void setupMouseEvents() {
        previewCanvas.setOnMousePressed(this::handleMousePressed);
        previewCanvas.setOnMouseDragged(this::handleMouseDragged);
        previewCanvas.setOnMouseReleased(this::handleMouseReleased);
    }

    private void handleMousePressed(MouseEvent event) {
        startX = event.getX();
        startY = event.getY();

        if (toolManager.getCurrentTool() == ToolManager.Tool.SPLASH) {
            executeSplashPaint((int) startX, (int) startY, toolManager.getCurrentColor());
            fireEvent(startX, startY, startX, startY, "SPLASH");
            return;
        }

        mainGc.setLineWidth(toolManager.getBrushSize());
        mainGc.setStroke(toolManager.getCurrentColor());

        if (toolManager.getCurrentTool() == ToolManager.Tool.PEN ||
                toolManager.getCurrentTool() == ToolManager.Tool.ERASER) {
            mainGc.beginPath();
            mainGc.moveTo(startX, startY);
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        double currentX = event.getX();
        double currentY = event.getY();

        if (toolManager.getCurrentTool() == ToolManager.Tool.PEN) {
            mainGc.lineTo(currentX, currentY);
            mainGc.stroke();
            fireEvent(startX, startY, currentX, currentY, "PEN");
            startX = currentX;
            startY = currentY;
        } else if (toolManager.getCurrentTool() == ToolManager.Tool.ERASER) {
            mainGc.setStroke(Color.WHITE);
            mainGc.lineTo(currentX, currentY);
            mainGc.stroke();
            fireEvent(startX, startY, currentX, currentY, "ERASER");
            startX = currentX;
            startY = currentY;
        } else {
            previewGc.clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
            previewGc.setLineWidth(toolManager.getBrushSize());
            previewGc.setStroke(toolManager.getCurrentColor());
            previewGc.setFill(Color.TRANSPARENT);

            if (toolManager.getCurrentTool() == ToolManager.Tool.RECTANGLE ||
                    toolManager.getCurrentTool() == ToolManager.Tool.TEXT) {
                double width = Math.abs(currentX - startX);
                double height = Math.abs(currentY - startY);
                double x = Math.min(startX, currentX);
                double y = Math.min(startY, currentY);

                if (toolManager.getCurrentTool() == ToolManager.Tool.TEXT) {
                    previewGc.setLineDashes(4);
                }
                previewGc.strokeRect(x, y, width, height);
                previewGc.setLineDashes(null);

            } else if (toolManager.getCurrentTool() == ToolManager.Tool.CIRCLE) {
                double radius = Math.hypot(currentX - startX, currentY - startY);
                previewGc.strokeOval(startX - radius, startY - radius, radius * 2, radius * 2);
            }
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        double endX = event.getX();
        double endY = event.getY();

        previewGc.clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
        mainGc.setLineWidth(toolManager.getBrushSize());
        mainGc.setStroke(toolManager.getCurrentColor());

        if (toolManager.getCurrentTool() == ToolManager.Tool.RECTANGLE) {
            double width = Math.abs(endX - startX);
            double height = Math.abs(endY - startY);
            double x = Math.min(startX, endX);
            double y = Math.min(startY, endY);
            mainGc.strokeRect(x, y, width, height);
            fireEvent(startX, startY, endX, endY, "RECTANGLE");

        } else if (toolManager.getCurrentTool() == ToolManager.Tool.CIRCLE) {
            double radius = Math.hypot(endX - startX, endY - startY);
            mainGc.strokeOval(startX - radius, startY - radius, radius * 2, radius * 2);
            fireEvent(startX, startY, endX, endY, "CIRCLE");

        } else if (toolManager.getCurrentTool() == ToolManager.Tool.TEXT) {
            double x = Math.min(startX, endX);
            double y = Math.min(startY, endY);
            double width = Math.max(120, Math.abs(endX - startX));
            double height = Math.max(60, Math.abs(endY - startY));

            TextArea textPad = new TextArea();
            textPad.setPrefSize(width, height);
            textPad.setLayoutX(x);
            textPad.setLayoutY(y);
            textPad.setWrapText(true);

            double fontSize = (toolManager.getBrushSize() * 1.5) + 12;
            String hexColor = toHexColor(toolManager.getCurrentColor());

            textPad.setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-control-inner-background: transparent; " +
                            "-fx-border-color: #0078D7; " +
                            "-fx-border-style: dashed; " +
                            "-fx-border-width: 1.5; " +
                            "-fx-text-fill: " + hexColor + "; " +
                            "-fx-font-family: 'Segoe UI', Arial; " +
                            "-fx-font-size: " + fontSize + "px;"
            );

            textPad.focusedProperty().addListener((observable, hadFocus, hasFocus) -> {
                if (!hasFocus) {
                    commitTextPadToCanvas(textPad, x, y, fontSize);
                }
            });

            overlayPane.getChildren().add(textPad);
            textPad.requestFocus();
        }
    }

    private void commitTextPadToCanvas(TextArea textPad, double x, double y, double fontSize) {
        String text = textPad.getText();
        if (text != null && !text.trim().isEmpty()) {
            mainGc.setFill(toolManager.getCurrentColor());
            mainGc.setFont(new Font("Segoe UI", fontSize));

            String[] lines = text.split("\n");
            double drawY = y + fontSize;

            for (String line : lines) {
                mainGc.fillText(line, x + 5, drawY);
                drawY += fontSize + 4;
            }

            if (onDrawEventCreated != null) {
                String networkPayloadText = text.replace("\n", " [NL] ");
                String colorHex = toHexColor(toolManager.getCurrentColor());
                onDrawEventCreated.accept(new DrawEvent(x, y, x + textPad.getWidth(), y + textPad.getHeight(), colorHex, toolManager.getBrushSize(), "TEXT:" + networkPayloadText));
            }
        }
        overlayPane.getChildren().remove(textPad);
    }

    // --- HELPER METHOD: FIRE NETWORK EVENT ---
    private void fireEvent(double x1, double y1, double x2, double y2, String tool) {
        if (onDrawEventCreated != null) {
            String colorHex = toHexColor(toolManager.getCurrentColor());
            onDrawEventCreated.accept(new DrawEvent(x1, y1, x2, y2, colorHex, toolManager.getBrushSize(), tool));
        }
    }

    private String toHexColor(Color c) {
        return String.format("#%02X%02X%02X", (int)(c.getRed()*255), (int)(c.getGreen()*255), (int)(c.getBlue()*255));
    }

    private void executeSplashPaint(int startX, int startY, Color replacementColor) {
        int width = (int) mainCanvas.getWidth();
        int height = (int) mainCanvas.getHeight();

        if (startX < 0 || startX >= width || startY < 0 || startY >= height) return;

        WritableImage snapshot = mainCanvas.snapshot(null, null);
        PixelReader reader = snapshot.getPixelReader();
        PixelWriter writer = mainGc.getPixelWriter();

        Color targetColor = reader.getColor(startX, startY);
        if (isColorMatch(targetColor, replacementColor)) return;

        Queue<Point> queue = new LinkedList<>();
        boolean[][] visited = new boolean[width][height];

        queue.add(new Point(startX, startY));
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            writer.setColor(p.x, p.y, replacementColor);

            int[] dx = {1, -1, 0, 0};
            int[] dy = {0, 0, 1, -1};

            for (int i = 0; i < 4; i++) {
                int nx = p.x + dx[i];
                int ny = p.y + dy[i];

                if (nx >= 0 && nx < width && ny >= 0 && ny < height && !visited[nx][ny]) {
                    if (isColorMatch(reader.getColor(nx, ny), targetColor)) {
                        visited[nx][ny] = true;
                        queue.add(new Point(nx, ny));
                    }
                }
            }
        }
    }

    private boolean isColorMatch(Color c1, Color c2) {
        double threshold = 0.08;
        return Math.abs(c1.getRed() - c2.getRed()) < threshold &&
                Math.abs(c1.getGreen() - c2.getGreen()) < threshold &&
                Math.abs(c1.getBlue() - c2.getBlue()) < threshold &&
                Math.abs(c1.getOpacity() - c2.getOpacity()) < threshold;
    }

    public void drawRemoteEvent(DrawEvent event) {
        mainGc.setLineWidth(event.strokeWidth);

        if (event.toolType.startsWith("TEXT:")) {
            String rawText = event.toolType.substring(5);
            String cleanText = rawText.replace(" [NL] ", "\n");
            mainGc.setFill(Color.valueOf(event.color));

            double calculatedFontSize = (event.strokeWidth * 1.5) + 12;
            mainGc.setFont(new Font("Segoe UI", calculatedFontSize));

            String[] lines = cleanText.split("\n");
            double remoteY = event.y1 + calculatedFontSize;
            for (String line : lines) {
                mainGc.fillText(line, event.x1 + 5, remoteY);
                remoteY += calculatedFontSize + 4;
            }
            return;
        }

        mainGc.setStroke(Color.valueOf(event.color));
        switch (event.toolType) {
            case "PEN":
            case "ERASER":
                if (event.toolType.equals("ERASER")) mainGc.setStroke(Color.WHITE);
                mainGc.strokeLine(event.x1, event.y1, event.x2, event.y2);
                break;
            case "RECTANGLE":
                double w = Math.abs(event.x2 - event.x1);
                double h = Math.abs(event.y2 - event.y1);
                mainGc.strokeRect(Math.min(event.x1, event.x2), Math.min(event.y1, event.y2), w, h);
                break;
            case "CIRCLE":
                double r = Math.hypot(event.x2 - event.x1, event.y2 - event.y1);
                mainGc.strokeOval(event.x1 - r, event.y1 - r, r * 2, r * 2);
                break;
            case "SPLASH":
                executeSplashPaint((int) event.x1, (int) event.y1, Color.valueOf(event.color));
                break;
        }
    }

    public void clearCanvas() {
        mainGc.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
        previewGc.clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
        overlayPane.getChildren().clear();
    }

    public void saveSnapshot() {
        WritableImage snapshot = new WritableImage((int) mainCanvas.getWidth(), (int) mainCanvas.getHeight());
        mainCanvas.snapshot(null, snapshot);

        if (undoHistory.size() == MAX_HISTORY) {
            undoHistory.remove(0); // Prevent OutOfMemory by dropping the oldest state
        }
        undoHistory.push(snapshot);
        redoHistory.clear(); // Drawing a new line permanently clears the "redo" future
    }

    public void undo() {
        if (!undoHistory.isEmpty()) {
            WritableImage currentSnapshot = new WritableImage((int) mainCanvas.getWidth(), (int) mainCanvas.getHeight());
            mainCanvas.snapshot(null, currentSnapshot);
            redoHistory.push(currentSnapshot);

            Image previousState = undoHistory.pop();
            mainGc.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
            mainGc.drawImage(previousState, 0, 0);
        }
    }

    public void redo() {
        if (!redoHistory.isEmpty()) {
            WritableImage currentSnapshot = new WritableImage((int) mainCanvas.getWidth(), (int) mainCanvas.getHeight());
            mainCanvas.snapshot(null, currentSnapshot);
            undoHistory.push(currentSnapshot);

            Image nextState = redoHistory.pop();
            mainGc.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
            mainGc.drawImage(nextState, 0, 0);
        }
    }

    public void clearHistory() {
        undoHistory.clear();
        redoHistory.clear();
    }

    public void saveCanvasToDisk() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Whiteboard");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File file = fileChooser.showSaveDialog(mainCanvas.getScene().getWindow());

        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int) mainCanvas.getWidth(), (int) mainCanvas.getHeight());
                mainCanvas.snapshot(null, writableImage);
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            } catch (Exception e) {
                System.err.println("Failed to save image: " + e.getMessage());
            }
        }
    }

    public void loadCanvasFromDisk() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Whiteboard Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File file = fileChooser.showOpenDialog(mainCanvas.getScene().getWindow());

        if (file != null) {
            saveSnapshot(); // Save current state in case they want to undo the load
            Image image = new Image(file.toURI().toString());
            mainGc.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
            mainGc.drawImage(image, 0, 0);
        }
    }

    public javafx.scene.image.Image getCanvasSnapshot() {
        WritableImage snapshot = new WritableImage((int) mainCanvas.getWidth(), (int) mainCanvas.getHeight());
        mainCanvas.snapshot(null, snapshot);
        return snapshot;
    }

    public void loadCanvasSnapshot(javafx.scene.image.Image snapshot) {
        clearCanvas(); // Always wipe the board first
        if (snapshot != null) {
            mainGc.drawImage(snapshot, 0, 0); // Stamp the saved draft back on
        }
    }

    private void drawGridPattern() {
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.setFill(Color.web("#d3d3d3")); // Professional light-grey
        for (int x = 0; x < gridCanvas.getWidth(); x += 20) {
            for (int y = 0; y < gridCanvas.getHeight(); y += 20) {
                gc.fillOval(x, y, 2, 2); // Small notebook-style dots
            }
        }
        gridCanvas.setVisible(false); // Hidden by default
    }

    public void setGridVisible(boolean visible) {
        gridCanvas.setVisible(visible);
    }

    // --- NEW: LIVE CURSOR METHODS ---
    public void updateRemoteCursor(String username, double x, double y, String colorHex) {
        // If the teammate doesn't have a cursor on screen yet, build one!
        Label cursor = remoteCursors.computeIfAbsent(username, k -> {
            Label newCursor = new Label("↖ " + username);
            newCursor.setStyle(
                    "-fx-background-color: " + colorHex + "; " +
                            "-fx-text-fill: white; " +
                            "-fx-padding: 2 6 2 6; " +
                            "-fx-background-radius: 4; " +
                            "-fx-font-size: 11px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 3, 0, 1, 1);"
            );
            overlayPane.getChildren().add(newCursor);
            return newCursor;
        });

        // Glide the cursor to the teammate's current mouse position
        cursor.setLayoutX(x);
        cursor.setLayoutY(y);
    }

    public void removeRemoteCursor(String username) {
        Label cursor = remoteCursors.remove(username);
        if (cursor != null) {
            overlayPane.getChildren().remove(cursor);
        }
    }
}