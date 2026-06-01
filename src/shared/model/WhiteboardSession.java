package shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WhiteboardSession implements Serializable {
    private String sessionId;
    private String name;
    private String type; // "PERSONAL" or "GROUP"
    private String ownerId;
    private List<User> participants = new ArrayList<>();

    public WhiteboardSession() {}

    public WhiteboardSession(String sessionId, String name, String type, String ownerId) {
        this.sessionId = sessionId;
        this.name = name;
        this.type = type;
        this.ownerId = ownerId;
    }

    public void addParticipant(User user) {
        if (participants.stream().noneMatch(u -> u.getUserId().equals(user.getUserId()))) {
            participants.add(user);
        }
    }

//    public void removeParticipant(String userId) {
//        participants.removeIf(u -> u.getUserId().equals(userId));
//    }
    public void removeParticipant(User user){
        participants.remove(user);
    }

    public String getSessionId() { return sessionId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getOwnerId() { return ownerId; }
    public List<User> getParticipants() { return participants; }
}

//package shared.model;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class WhiteboardSession {
//
//    private String sessionId;
//    private String name;
//    private String type;
//    private String ownerId;
//
//    private final List<User> participants = new ArrayList<>();
//
//    public WhiteboardSession() {
//    }
//
//    public WhiteboardSession(String sessionId, String name, String type, String ownerId) {
//        this.sessionId = sessionId;
//        this.name = name;
//        this.type = type;
//        this.ownerId = ownerId;
//    }
//
//    public void addParticipant(User user) {
//        participants.add(user);
//    }
//
//    public void removeParticipant(User user) {
//        participants.remove(user);
//    }
//
//    public List<User> getParticipants() {
//        return participants;
//    }
//
//    public String getSessionId() {
//        return sessionId;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public String getOwnerId() {
//        return ownerId;
//    }
//}

//public class WhiteboardSession {
//
//    public String getSessionId() {
//        return "SessionId";
//    }
//
//    public String getName() {
//        return "Name";
//    }
//
//    public String getType() {
//        return "Type";
//    }
//
//    public String getOwnerId() {
//        return "OwnerId";
//    }
//
//    public void addParticipant(User user) {
//    }
//
//    public void removeParticipant(User user) {
//    }
//}
