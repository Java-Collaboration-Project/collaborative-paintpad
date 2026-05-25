package shared;

public class DrawEvent {
    public double x1, y1, x2, y2;
    public String color;
    public double strokeWidth;
    public String toolType;

    public DrawEvent(double x1, double y1, double x2, double y2, String color, double strokeWidth, String toolType) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.toolType = toolType;
    }
}
