package shared.protocol;

public enum EventType {
    LOGIN,
    DRAW,
    UNDO,
    REDO,
    JOIN_SESSION,
    LEAVE_SESSION,
    CLEAR_BOARD,
    SYNC_BOARD,
    REPLAY,
    REPLAY_BOARD
}