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
    public void createUser(String nome) throws SQLException {
        User user = new User(nome);
        userRepo.save(user);
        System.out.println("Usuário criado: " + user.getName());
    }

    public List<User> listUsers() throws SQLException {
        return userRepo.findAll();
    }

    public User findUserByName(String nome) throws SQLException {
        return userRepo.findByName(nome);
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
    public boolean linkUserTask(int userId, int taskId) throws SQLException {
        return taskRepo.linkUserTask(userId, taskId);
    }

    // Teste de conexão
    public void pingDatabase() throws SQLException {
        taskRepo.pingDatabase();
    }
}