package shared.model;

public class User {

    private String userId;
    private String username;
    private boolean online;

    public User() {
    }

    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;
        this.online = true;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}