package server.service;

import shared.model.WhiteboardAction;

import java.util.Stack;

public class ActionHistoryService {

    private final Stack<WhiteboardAction> undoStack = new Stack<>();
    private final Stack<WhiteboardAction> redoStack = new Stack<>();

    public void recordAction(WhiteboardAction action) {
        undoStack.push(action);
        redoStack.clear();
    }

    public WhiteboardAction undo() {

        if (undoStack.isEmpty()) {
            return null;
        }

        WhiteboardAction action = undoStack.pop();
        redoStack.push(action);

        return action;
    }

    public WhiteboardAction redo() {

        if (redoStack.isEmpty()) {
            return null;
        }

        WhiteboardAction action = redoStack.pop();
        undoStack.push(action);

        return action;
    }
}