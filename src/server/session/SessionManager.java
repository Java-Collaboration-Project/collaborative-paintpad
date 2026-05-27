package server.session;

import shared.model.WhiteboardSession;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    private final Map<String, WhiteboardSession> activeSessions = new HashMap<>();

    public void createSession(WhiteboardSession session) {
        activeSessions.put(session.getSessionId(), session);
    }

    public WhiteboardSession getSession(String sessionId) {
        return activeSessions.get(sessionId);
    }

    public void removeSession(String sessionId) {
        activeSessions.remove(sessionId);
    }

    public Map<String, WhiteboardSession> getActiveSessions() {
        return activeSessions;
    }
}