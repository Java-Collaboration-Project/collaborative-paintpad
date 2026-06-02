package client.draw;

import javafx.scene.paint.Color;

public class ToolManager {
    public enum Tool { PEN, ERASER, RECTANGLE, CIRCLE, TEXT, SPLASH }

    private Tool currentTool = Tool.PEN;
    private Color currentColor = Color.BLACK;
    private double brushSize = 5.0;

    public Tool getCurrentTool() { return currentTool; }
    public void setCurrentTool(Tool currentTool) { this.currentTool = currentTool; }

    public Color getCurrentColor() { return currentColor; }
    public void setCurrentColor(Color currentColor) { this.currentColor = currentColor; }

    public double getBrushSize() { return brushSize; }
    public void setBrushSize(double brushSize) { this.brushSize = brushSize; }
}