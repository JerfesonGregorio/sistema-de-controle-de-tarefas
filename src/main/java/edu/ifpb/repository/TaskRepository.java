package edu.ifpb.repository;

import edu.ifpb.state.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                System.out.println("Banco está conectado!");
            }
        }
    }

    public Task findById(int id) throws SQLException {
        String sql = "SELECT id, name, status FROM tasks WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToTask(rs);
            }
        }
        return null;
    }

    public void save(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (name, status) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, task.getName());
            ps.setString(2, task.getStatus());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int taskId = rs.getInt(1);
                    task.setId(taskId);
                }
            }
        }
    }

    public boolean deleteById(int id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Task> findAll() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, name, status FROM tasks";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String status = rs.getString("status");
                Task task = new Task(name);
                switch (status) {
                    case "Pendente": break;
                    case "Em andamento": task.setState(new InProgressState()); break;
                    case "Concluída": task.setState(new CompletedState()); break;
                }
                task.setId(id);
                tasks.add(task);
            }
        }
        return tasks;
    }

    public boolean linkUserTask(int userId, int taskId) throws SQLException {
        String sql = "INSERT INTO user_tasks (user_id, task_id) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, taskId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Task> findAvailable() throws SQLException {
        return findByStatus("Pendente");
    }

    public List<Task> findInProgress() throws SQLException {
        return findByStatus("Em andamento");
    }

    public List<Task> findCompleted() throws SQLException {
        return findByStatus("Concluída");
    }

    private List<Task> findByStatus(String status) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = """
        SELECT t.id, t.name, t.status, u.name AS user_name
        FROM tasks t
        LEFT JOIN user_tasks ut ON t.id = ut.task_id
        LEFT JOIN users u ON ut.user_id = u.id
        WHERE t.status = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapRowToTask(rs));
                }
            }
        }
        return tasks;
    }

    private Task mapRowToTask(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String status = rs.getString("status");

        Task task = new Task(name);
        switch (status) {
            case "Pendente": break;
            case "Em andamento": task.setState(new InProgressState()); break;
            case "Concluída": task.setState(new CompletedState()); break;
        }
        task.setId(id);
        return task;
    }

    public boolean isTaskLinkedToUser(int userId, int taskId) throws SQLException {
        String sql = "SELECT 1 FROM user_tasks WHERE user_id = ? AND task_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, taskId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }


    public boolean updateTaskStatusByUser(int userId, int taskId, String newStatus) throws SQLException {
        if (!isTaskLinkedToUser(userId, taskId)) {
            return false;
        }

        String sql = "UPDATE tasks SET status = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, taskId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Task> findTasksByUser(int userId, boolean includeCompleted) throws SQLException {
        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT t.id, t.name, t.status " +
                "FROM tasks t " +
                "JOIN user_tasks ut ON t.id = ut.task_id " +
                "WHERE ut.user_id = ?";

        if (!includeCompleted) {
            sql += " AND t.status <> 'Concluída'";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapRowToTask(rs));
                }
            }
        }

        return tasks;
    }

    public boolean unlinkUserTask(int userId, int taskId) throws SQLException {
        String sql = "DELETE FROM user_tasks WHERE user_id = ? AND task_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, taskId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Task> findAllWithUsers() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = """
        SELECT t.id, t.name, t.status, u.name AS user_name
        FROM tasks t
        LEFT JOIN user_tasks ut ON t.id = ut.task_id
        LEFT JOIN users u ON ut.user_id = u.id
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setName(rs.getString("name"));
                task.setState(createStateFromDb(rs.getString("status")));

                String assignedUser = rs.getString("user_name");
                if (assignedUser != null) {
                    task.setAssignedUser(assignedUser);
                }

                tasks.add(task);
            }
        }
        return tasks;
    }

    private TaskState createStateFromDb(String statusDb) {
        return switch (statusDb.toLowerCase()) {
            case "a fazer", "to-do" -> new ToDoState();
            case "em progresso", "in-progress" -> new InProgressState();
            case "concluída", "done" -> new CompletedState();
            default -> new ToDoState();
        };
    }

}
