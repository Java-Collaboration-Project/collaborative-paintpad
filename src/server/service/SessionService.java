package server.service;

import server.db.SessionRepository;
import server.session.SessionManager;
import shared.model.WhiteboardSession;

public class SessionService {

    private final SessionManager sessionManager;
    private final SessionRepository sessionRepository;

    public SessionService(SessionManager sessionManager, SessionRepository sessionRepository) {

        this.sessionManager = sessionManager;
        this.sessionRepository = sessionRepository;
    }

    public void createSession(WhiteboardSession session) {

        sessionManager.createSession(session);
        sessionRepository.createSession(session);
    }

    public WhiteboardSession getSession(String sessionId) {
        return sessionManager.getSession(sessionId);
    }
}