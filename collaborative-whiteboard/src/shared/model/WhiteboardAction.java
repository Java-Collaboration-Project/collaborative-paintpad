package shared.model;

import shared.enums.ActionType;

public class WhiteboardAction {

    private String actionId;
    private ActionType actionType;
    private DrawEvent drawEvent;
    private String userId;
    private long timestamp;

    public WhiteboardAction() {
    }

    public WhiteboardAction(String actionId, ActionType actionType, DrawEvent drawEvent, String userId, long timestamp) {

        this.actionId = actionId;
        this.actionType = actionType;
        this.drawEvent = drawEvent;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public String getActionId() {
        return actionId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public DrawEvent getDrawEvent() {
        return drawEvent;
    }

    public String getUserId() {
        return userId;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

//import shared.enums.ActionType;
//
//import java.time.LocalDate;
//
//public class WhiteboardAction {
//    public String getActionId() {
//        return "ActionId";
//    }
//
//    public DrawEvent getDrawEvent() {
//        return new DrawEvent(0.0,0.0,0.0,0.0,"color",0.0,"pen");
//    }
//
//    public ActionType getActionType() {
//        return ActionType.UNDO;
//    }
//
//    public long getTimestamp() {
//        return LocalDate.now().toEpochDay();
//    }
//}
