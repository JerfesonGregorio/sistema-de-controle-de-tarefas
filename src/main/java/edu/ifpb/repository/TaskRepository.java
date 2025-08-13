package edu.ifpb.repository;

import edu.ifpb.state.Task;

import java.sql.*;

public class TaskRepository {

    private final Connection connection;

    public TaskRepository(Connection connection) {
        this.connection = connection;
    }

    public void pingDatabase() throws SQLException {

        String sql = "SELECT 1;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.execute();

            ResultSet rs = ps.getResultSet();
            if(rs.next()) {
                int ping = rs.getInt(1);
                System.out.println(ping);
            }
        }
    }

    public void save(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (name, status) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, task.getName());
            ps.setString(2, task.getStatus());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int taskId = rs.getInt(1);
                task.setId(taskId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
