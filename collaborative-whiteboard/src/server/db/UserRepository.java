package server.db;

import shared.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserRepository {

    public void saveUser(User user) {

        String sql = "INSERT INTO users(id, username, online) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUserId());
            statement.setString(2, user.getUsername());
            statement.setBoolean(3, true);

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateUserStatus(String userId, boolean online) {

        String sql = "UPDATE users SET online = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, online);
            stmt.setString(2, userId);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}