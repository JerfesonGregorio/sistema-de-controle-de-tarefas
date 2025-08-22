package edu.ifpb.ui;

import edu.ifpb.Util;
import edu.ifpb.facade.TaskManagementFacade;
import edu.ifpb.state.Task;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class TaskMenu implements Menu {

    private final TaskManagementFacade facade;
    private final Scanner scanner = new Scanner(System.in);
    private final Util util = new Util();

    public TaskMenu(TaskManagementFacade facade) {
        this.facade = facade;
    }

    @Override
    public void show() {
        System.out.println("\n=== ✅ MENU TAREFAS ===");
        System.out.println("1. Criar tarefa");
        System.out.println("2. Listar todas as tarefas");
        System.out.println("3. Listar tarefas disponíveis");
        System.out.println("4. Listar tarefas em andamento");
        System.out.println("5. Listar tarefas concluídas");
        System.out.println("6. Listar tarefas com usuários");
        System.out.println("7. Remover tarefa");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
    }

    @Override
    public void handleInput(String input) {
        try {
            switch (input) {
                case "1":
                    System.out.print("Digite o nome da tarefa: ");
                    String nome = scanner.nextLine();
                    facade.createTask(nome);
                    break;
                case "2":
                    util.list(facade.listTasks(), "TODAS AS TAREFAS");
                    break;
                case "3":
                    util.list(facade.listAvailableTasks(), "TAREFAS DISPONÍVEIS (Pendentes)");
                    break;
                case "4":
                    util.list(facade.listInProgressTasks(), "TAREFAS EM ANDAMENTO");
                    break;
                case "5":
                    util.list(facade.listCompletedTasks(), "TAREFAS CONCLUÍDAS");
                    break;
                case "6":
                    util.listWithUsers(facade.listAllTasksWithUsers(), "TAREFAS COM USUÁRIOS");
                    break;
                case "7":
                    System.out.print("Digite o ID da tarefa para remover: ");
                    int taskId = Integer.parseInt(scanner.nextLine());
                    boolean taskRemoved = facade.removeTask(taskId);
                    System.out.println(taskRemoved ? "✅ Tarefa removida!" : "❌ Tarefa não encontrada.");
                    break;
                case "0":
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
