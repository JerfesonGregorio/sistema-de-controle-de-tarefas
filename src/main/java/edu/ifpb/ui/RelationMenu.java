package edu.ifpb.ui;

import edu.ifpb.facade.TaskManagementFacade;

import java.sql.SQLException;
import java.util.Scanner;

public class RelationMenu implements Menu {

    private final TaskManagementFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    public RelationMenu(TaskManagementFacade facade) {
        this.facade = facade;
    }

    @Override
    public void show() {
        System.out.println("\n=== 🔗 MENU RELACIONAMENTO ===");
        System.out.println("1. Atribuir tarefa a usuário");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
    }

    @Override
    public void handleInput(String input) {
        try {
            switch (input) {
                case "1":
                    System.out.print("Digite o ID do usuário: ");
                    int userId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Digite o ID da tarefa: ");
                    int taskId = Integer.parseInt(scanner.nextLine());
                    boolean ok = facade.linkUserTask(userId, taskId);
                    System.out.println(ok ? "Tarefa atribuída com sucesso!" : "Falha ao atribuir tarefa.");
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
