package server.service;

import server.db.UserRepository;
import shared.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private final UserRepository userRepository;

    private final Map<String, User> onlineUsers = new HashMap<>();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(User user) {

        userRepository.saveUser(user);

        onlineUsers.put(user.getUserId(), user);
    }

    public User getOnlineUser(String userId) {
        return onlineUsers.get(userId);
    }

    public void removeOnlineUser(String userId) {
        onlineUsers.remove(userId);
    }
}