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
    // Padrão: Facade
    // Onde aplicado: toda a classe
    // Como implementado: fornece uma interface simplificada para interagir com usuários, tarefas e relacionamentos.
    // Por que foi escolhido: abstrai a complexidade da camada de repositório, oferecendo métodos de alto nível para a aplicação.
    private final UserRepository userRepo;
    private final TaskRepository taskRepo;

    public TaskManagementFacade() throws SQLException {
        // Padrão: Singleton + Repository
        // Onde aplicado: ConfigManager.getInstance()
        // Como implementado: obtém conexão única do banco de dados.
        // Por que foi escolhido: garante que todos os repositórios compartilhem a mesma conexão.
        Connection conn = ConfigManager.getInstance().getConnection();
        this.userRepo = new UserRepository(conn);
        this.taskRepo = new TaskRepository(conn);
    }

    // Padrão: Facade + Repository
    // Onde aplicado: createUser, listUsers, findUserByName
    // Como implementado: delega operações do usuário aos métodos de UserRepository.
    // Por que foi escolhido: simplifica a criação e busca de usuários, escondendo detalhes de persistência.
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

    // Padrão: Facade + Repository
    // Onde aplicado: createTask, listTasks, listAvailableTasks, listInProgressTasks, listCompletedTasks
    // Como implementado: delega operações do TaskRepository, incluindo filtros de status.
    // Por que foi escolhido: oferece interface limpa para manipular tarefas sem expor repositório.
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

            user.assignTask(task);
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

    // Padrão: Facade + State
    // Onde aplicado: updateTaskStatusByUser
    // Como implementado: delega atualização de status para o objeto Task usando padrão State.
    // Por que foi escolhido: separa lógica de transição de estados do banco, centralizando fluxo no objeto Task.
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
                task.nextState();
            } else {
                task.previousState();
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