package server.session;

import shared.model.WhiteboardSession;

public class PersonalSessionManager {

    private final SessionManager sessionManager;

    public PersonalSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public WhiteboardSession createPersonalBoard(WhiteboardSession session) {

        sessionManager.createSession(session);

        return session;
    }
}