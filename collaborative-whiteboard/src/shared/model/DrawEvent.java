package shared.model;

import shared.enums.ToolType;

public class DrawEvent {

    public String actionId; // private
    public String sessionId;
    public String userId;
    public String toolType; //for mera's canva class needs to be removed

    public ToolType tool;

    public double x1;
    public double y1;
    public double x2;
    public double y2;

    public String color;
    public double strokeWidth;

    public long timestamp;

    public DrawEvent() {}
    public DrawEvent(double x1, double y1, double x2, double y2, String color, double strokeWidth, String toolType) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.toolType = toolType;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ToolType getTool() {
        return tool;
    }

    public void setTool(ToolType tool) {
        this.tool = tool;
    }

    public double getX1() {
        return x1;
    }
    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

//import shared.enums.ToolType;
//import java.time.LocalDate;
//
//public class DrawEvent {
//    public double x1, y1, x2, y2;
//    public String color;
//    public double strokeWidth;
//    public String toolType;
//
//    public DrawEvent(double x1, double y1, double x2, double y2, String color, double strokeWidth, String toolType) {
//        this.x1 = x1;
//        this.y1 = y1;
//        this.x2 = x2;
//        this.y2 = y2;
//        this.color = color;
//        this.strokeWidth = strokeWidth;
//        this.toolType = toolType;
//    }
//
//    public String getActionId() {
//        return "ActionId";
//    }
//
//    public String getSessionId() {
//        return "SessionId";
//    }
//
//    public String getUserId() {
//        return "UserId";
//    }
//
//    public ToolType getTool() {
//        return ToolType.PEN;
//    }
//
//    public String getColor() {
//        return "Color";
//    }
//
//    public double getStrokeWidth() {
//        return 0.0;
//    }
//
//    public double getX1() {
//        return 0.0;
//    }
//    public double getY1() {
//        return 0.0;
//    }
//    public double getX2() {
//        return 0.0;
//    }
//    public double getY2() {
//        return 0.0;
//    }
//
//    public long getTimestamp() {
//        return LocalDate.now().toEpochDay(); // i don't know what it supposed to do but it returns long so its fine
//    }
//
//    public void setActionId(String id) {
//    }
//
//    public void setSessionId(String sessionId) {
//    }
//
//    public void setUserId(String userId) {
//    }
//
//    public void setColor(String color) {
//    }
//
//    public void setStrokeWidth(double strokeWidth) {
//    }
//
//    public void setX1(double x1) {
//    }
//
//    public void setY1(double y1) {
//    }
//
//    public void setX2(double x2) {
//    }
//
//    public void setY2(double y2) {
//    }
//
//    public void setTimestamp(long timestamp) {
//    }
//}
