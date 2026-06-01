package shared.model;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String username;
    private boolean online;

    public User() {}

    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;
        this.online = true;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }
}

//package shared.model;
//
//public class User {
//
//    private String userId;
//    private String username;
//    private boolean online;
//
//    public User() {
//    }
//
//    public User(String userId, String username) {
//        this.userId = userId;
//        this.username = username;
//        this.online = true;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public boolean isOnline() {
//        return online;
//    }
//
//    public void setOnline(boolean online) {
//        this.online = online;
//    }
//}