package shared.protocol;

public class Message {
    private EventType type;
    private String sessionId;
    private String senderId;
    private long timestamp;
    private Object payload;

    public Message() {}

    public Message(EventType type, String sessionId, String senderId, Object payload) {
        this.type = type;
        this.sessionId = sessionId;
        this.senderId = senderId;
        this.payload = payload;
        this.timestamp = System.currentTimeMillis();
    }

    public EventType getType() { return type; }
    public String getSessionId() { return sessionId; }
    public String getSenderId() { return senderId; }
    public long getTimestamp() { return timestamp; }
    public Object getPayload() { return payload; }
}
