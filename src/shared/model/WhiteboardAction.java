package shared.model;

import shared.enums.ActionType;
import java.io.Serializable;

public class WhiteboardAction implements Serializable {
    private String actionId;
    private ActionType actionType;
    private DrawEvent drawEvent;
    private String userId;
    private long timestamp;

    public WhiteboardAction() {}

    public WhiteboardAction(String actionId, ActionType actionType, DrawEvent drawEvent, String userId) {
        this.actionId = actionId;
        this.actionType = actionType;
        this.drawEvent = drawEvent;
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
    }

    public String getActionId() { return actionId; }
    public ActionType getActionType() { return actionType; }
    public DrawEvent getDrawEvent() { return drawEvent; }
    public String getUserId() { return userId; }
    public long getTimestamp() { return timestamp; }
}
