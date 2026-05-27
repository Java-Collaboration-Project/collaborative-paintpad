package server.session;

import shared.model.User;
import shared.model.WhiteboardSession;

public class GroupSessionManager {

    private final SessionManager sessionManager;

    public GroupSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void addUserToGroup(String sessionId, User user) {

        WhiteboardSession session = sessionManager.getSession(sessionId);

        if (session != null) {
            session.addParticipant(user);
        }
    }

    public void removeUserFromGroup(String sessionId, User user) {

        WhiteboardSession session = sessionManager.getSession(sessionId);

        if (session != null) {
            session.removeParticipant(user);
        }
    }
}