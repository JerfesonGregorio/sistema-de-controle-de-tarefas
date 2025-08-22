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

    public User findUserByName(String name) {

        try {
            String formatName = name.trim().toLowerCase();
            return userRepo.findByName(formatName);
        } catch (Exception e) {
            System.out.println("Usuário não encontrado!");
            return null;
        }
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
    public boolean linkUserTask(String userName, String taskIdInput) {
        try {
            if (!taskIdInput.matches("\\d+")) {
                System.out.println("❌ O ID da tarefa deve conter apenas números!");
                return false;
            }
            int taskId = Integer.parseInt(taskIdInput);

            User user = findUserByName(userName);
            if (user == null) {
                System.out.println("❌ Usuário não encontrado no banco!");
                return false;
            }

            Task task = taskRepo.findById(taskId);
            if (task == null) {
                System.out.println("❌ Tarefa não encontrada no banco!");
                return false;
            }

            task.setAssignedUser(userName);
            return taskRepo.linkUserTask(user.getId(), taskId);

        } catch (SQLException e) {
            System.out.println("⚠️ Erro ao atribuir tarefa: " + e.getMessage());
            return false;
        }
    }

    public boolean removeTask(int taskId) {
        try {
            return taskRepo.deleteById(taskId);
        } catch (SQLException e) {
            System.out.println("❌ Erro ao remover tarefa: " + e.getMessage());
            return false;
        }
    }

    public boolean removeUser(String userName) {
        try {
            return userRepo.deleteByName(userName);
        } catch (SQLException e) {
            System.out.println("❌ Erro ao remover usuário: " + e.getMessage());
            return false;
        }
    }

    public List<Task> listAvailableTasks() throws SQLException {
        return taskRepo.findAvailable();
    }

    public List<Task> listInProgressTasks() throws SQLException {
        return taskRepo.findInProgress();
    }

    public List<Task> listCompletedTasks() throws SQLException {
        return taskRepo.findCompleted();
    }

    // Teste de conexão
    public void pingDatabase() throws SQLException {
        taskRepo.pingDatabase();
    }

    public boolean updateTaskStatusByUser(String userName, int taskId, boolean next) {
        try {
            User user = findUserByName(userName);
            if (user == null) {
                System.out.println("❌ Usuário não encontrado!");
                return false;
            }

            Task task = taskRepo.findById(taskId);
            if (task == null) {
                System.out.println("❌ Tarefa não encontrada!");
                return false;
            }

            // Confere se está vinculada
            if (!taskRepo.isTaskLinkedToUser(user.getId(), taskId)) {
                System.out.println("❌ Essa tarefa não pertence ao usuário!");
                return false;
            }

            // Atualiza o estado via State
            if (next) {
                task.getState().next(task);
            } else {
                task.getState().prev(task);
            }

            // Persiste no banco
            return taskRepo.updateTaskStatusByUser(user.getId(), taskId, task.getState().getStatus());

        } catch (SQLException e) {
            System.out.println("⚠️ Erro ao atualizar status: " + e.getMessage());
            return false;
        }
    }

    public List<Task> listTasksByUserExcludingCompleted(String userName) {
        try {
            User user = findUserByName(userName);
            if (user == null) {
                System.out.println("❌ Usuário não encontrado.");
                return List.of();
            }

            List<Task> tasks = taskRepo.findTasksByUser(user.getId(), false);
            return tasks.stream()
                    .filter(t -> !"Concluída".equalsIgnoreCase(t.getStatus()))
                    .toList();
        } catch (SQLException e) {
            System.out.println("Erro ao buscar tarefas do usuário: " + e.getMessage());
            return List.of();
        }
    }

    public boolean unlinkTaskFromUser(String userName, int taskId) throws SQLException {
        User user = userRepo.findByName(userName);
        if (user == null) {
            System.out.println("Usuário não encontrado!");
            return false;
        }
        return taskRepo.unlinkUserTask(user.getId(), taskId);
    }

    public List<Task> listAllTasksWithUsers() throws SQLException {
        return taskRepo.findAllWithUsers();
    }
}