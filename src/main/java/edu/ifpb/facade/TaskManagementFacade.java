package edu.ifpb.facade;

import edu.ifpb.observer.User;
import edu.ifpb.repository.TaskRepository;
import edu.ifpb.repository.UserRepository;
import edu.ifpb.singleton.ConfigManager;
import edu.ifpb.state.Task;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TaskManagementFacade {

    private final UserRepository userRepo;
    private final TaskRepository taskRepo;

    public TaskManagementFacade() throws SQLException {
        Connection conn = ConfigManager.getInstance().getConnection();
        this.userRepo = new UserRepository(conn);
        this.taskRepo = new TaskRepository(conn);
    }

    // Usuário
    public void createUser(String name) throws SQLException {
        String formatName = name.trim().toLowerCase();
        User user = new User(formatName);
        userRepo.save(user);
        System.out.println("Usuário criado: " + user.getName());
    }

    public List<User> listUsers() throws SQLException {
        return userRepo.findAll();
    }

    public User findUserByName(String name) throws SQLException {
        String formatName = name.trim().toLowerCase();
        return userRepo.findByName(formatName);
    }

    // Tarefa
    public void createTask(String nome) throws SQLException {
        Task task = new Task(nome);
        taskRepo.save(task);
        System.out.println("Tarefa criada: " + task.getName());
    }

    public List<Task> listTasks() throws SQLException {
        return taskRepo.findAll();
    }

    // Relacionamento Usuário ↔ Tarefa
    public boolean linkUserTask(String userName, int taskId) {
        try {
            User user = findUserByName(userName);
            return taskRepo.linkUserTask(user.getId(), taskId);
        } catch (SQLException e) {
            System.err.print("Não foi possível atribuir tarefa a usuário!");
            return false;
        }
    }

    // Teste de conexão
    public void pingDatabase() throws SQLException {
        taskRepo.pingDatabase();
    }
}