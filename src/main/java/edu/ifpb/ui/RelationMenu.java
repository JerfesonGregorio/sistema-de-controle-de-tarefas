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

            case "0":
                System.out.println("Voltando...");
                break;

            default:
                System.out.println("Opção inválida!");
        }
    }
}
