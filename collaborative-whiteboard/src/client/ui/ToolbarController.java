package client.ui;

import client.draw.CanvasManager;
import client.draw.ToolManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ToolbarController {
    private final HBox toolbar;
    private final ToolManager toolManager;
    private final CanvasManager canvasManager;

    public ToolbarController(ToolManager toolManager, CanvasManager canvasManager) {
        this.toolManager = toolManager;
        this.canvasManager = canvasManager;
        this.toolbar = new HBox(10);
        setupToolbar();
    }

    public HBox getToolbar() { return toolbar; }

    private void setupToolbar() {
        toolbar.setPadding(new Insets(10));
        toolbar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        ToggleGroup toolGroup = new ToggleGroup();
        RadioButton penBtn = createToolButton("Pen", ToolManager.Tool.PEN, toolGroup, true);
        RadioButton eraserBtn = createToolButton("Eraser", ToolManager.Tool.ERASER, toolGroup, false);
        RadioButton rectBtn = createToolButton("Rectangle", ToolManager.Tool.RECTANGLE, toolGroup, false);
        RadioButton circleBtn = createToolButton("Circle", ToolManager.Tool.CIRCLE, toolGroup, false);
        RadioButton textBtn = createToolButton("Text Box", ToolManager.Tool.TEXT, toolGroup, false);
        RadioButton splashBtn = createToolButton("Splash Paint", ToolManager.Tool.SPLASH, toolGroup, false);

        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setOnAction(e -> toolManager.setCurrentColor(colorPicker.getValue()));

        Label sizeLabel = new Label("Size:");
        Slider sizeSlider = new Slider(1, 20, 5);
        sizeSlider.setShowTickMarks(true);
        sizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> toolManager.setBrushSize(newVal.doubleValue()));

        Button clearBtn = new Button("Clear Canvas");
        clearBtn.setOnAction(e -> canvasManager.clearCanvas());

        toolbar.getChildren().addAll(penBtn, eraserBtn, rectBtn, circleBtn, textBtn, splashBtn,
                new Separator(), colorPicker, new Separator(), sizeLabel, sizeSlider,
                new Separator(), clearBtn);
    }

    private RadioButton createToolButton(String text, ToolManager.Tool tool, ToggleGroup group, boolean selected) {
        RadioButton btn = new RadioButton(text);
        btn.setToggleGroup(group);
        btn.setSelected(selected);
        btn.setOnAction(e -> toolManager.setCurrentTool(tool));
        return btn;
    }
}