package edu.ifpb.repository;

import edu.ifpb.observer.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users (name) VALUES (?) ON CONFLICT (name) DO NOTHING";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getName());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                user.setId(generatedId);
            }
        }
    }

    public User findByName(String name) throws SQLException {
        String sql = "SELECT id, name FROM users WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User(rs.getString("name"));
                user.setId(rs.getInt("id"));
                return user;
            }
        }
        return null;
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name FROM users";
        try (Statement stmt = connection.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                User user = new User(rs.getString("name"));
                user.setId(rs.getInt("id"));
                users.add(user);
            }
        }
        return users;
    }



}
