package edu.ifpb.ui;

import edu.ifpb.facade.TaskManagementFacade;
import edu.ifpb.state.Task;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class RelationMenu implements Menu {

    private final TaskManagementFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    public RelationMenu(TaskManagementFacade facade) {
        this.facade = facade;
    }

    @Override
    public void show() {
        System.out.println("\n=== 🔗 MENU DE ATRIBUIÇÃO ===");
        System.out.println("1. Atribuir tarefa a usuário");
        System.out.println("2. Atualizar status de tarefa do usuário");
        System.out.println("3. Desvincular tarefa de usuário");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
    }

    @Override
    public void handleInput(String input) {
        switch (input) {
            case "1":
                try {
                    List<Task> availableTasks = facade.listAvailableTasks();
                    System.out.println("\n########## TAREFAS DISPONÍVEIS ##########");
                    if (availableTasks.isEmpty()) {
                        System.out.println("(nenhuma tarefa disponível no momento)");
                        break;
                    } else {
                        availableTasks.forEach(t ->
                                System.out.println(t.getId() + " - " + t.getName() + " | Status: " + t.getStatus())
                        );
                    }

                    System.out.print("\nDigite o nome do usuário: ");
                    String userName = scanner.nextLine();

                    System.out.print("Digite o ID da tarefa: ");
                    String taskId = scanner.nextLine();

                    boolean ok = facade.linkUserTask(userName, taskId);
                    System.out.println(ok ? "✅ Tarefa atribuída com sucesso!" : "❌ Falha ao atribuir tarefa.");
                } catch (SQLException e) {
                    System.out.println("Erro ao listar tarefas: " + e.getMessage());
                }
                break;

            case "2":
                System.out.print("Digite o nome do usuário logado: ");
                String userName = scanner.nextLine();

                List<Task> userTasks = facade.listTasksByUserExcludingCompleted(userName);

                if (userTasks.isEmpty()) {
                    System.out.println("⚠️ Esse usuário não possui tarefas pendentes/em andamento.");
                    break;
                }

                System.out.println("\n########## SUAS TAREFAS ##########");
                userTasks.forEach(t ->
                        System.out.println("#" + t.getId() + " - " + t.getName() + " | Status: " + t.getStatus())
                );

                int taskId = -1;
                while (true) {
                    System.out.print("Digite o ID da tarefa a atualizar: ");
                    input = scanner.nextLine();

                    try {
                        taskId = Integer.parseInt(input);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Entrada inválida! Digite apenas números.");
                    }
                }

                while (true) {
                    System.out.print("Avançar status (true) ou voltar status (false)? ");
                    input = scanner.nextLine();

                    try {
                        if(input.equals("true") || input.equals("false")) {
                            boolean next = Boolean.parseBoolean(scanner.nextLine());
                            boolean ok = facade.updateTaskStatusByUser(userName, taskId, next);
                            System.out.println(ok ? "✅ Status atualizado!" : "❌ Não foi possível atualizar.");
                            break;
                        }
                        throw new Exception();
                    } catch (Exception e) {
                        System.out.println("❌ Entrada inválida! Digite apenas (true) ou (false).");
                    }
                }
                break;

            case "3":
                System.out.print("Digite o nome do usuário: ");
                String userName4 = scanner.nextLine();

                List<Task> userTasks2 = facade.listTasksByUserExcludingCompleted(userName4);

                if (userTasks2.isEmpty()) {
                    System.out.println("⚠️ Esse usuário não possui tarefas pendentes/em andamento.");
                    break;
                }

                System.out.println("\n########## SUAS TAREFAS ##########");
                userTasks2.forEach(t ->
                        System.out.println("#" + t.getId() + " - " + t.getName() + " | Status: " + t.getStatus())
                );

                int taskIdUnlink;
                while (true) {
                    System.out.print("Digite o ID da tarefa a desvincular: ");
                    String inputTask = scanner.nextLine();
                    try {
                        taskIdUnlink = Integer.parseInt(inputTask);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Entrada inválida! Digite apenas números.");
                    }
                }

                try {
                    boolean ok = facade.unlinkTaskFromUser(userName4, taskIdUnlink);
                    System.out.println(ok ? "✅ Tarefa desvinculada do usuário." : "❌ Não foi possível desvincular.");
                } catch (SQLException e) {
                    System.out.println("Erro ao desvincular: " + e.getMessage());
                }
                break;

            case "0":
                System.out.println("Voltando...");
                break;

            default:
                System.out.println("Opção inválida!");
        }
    }
}
