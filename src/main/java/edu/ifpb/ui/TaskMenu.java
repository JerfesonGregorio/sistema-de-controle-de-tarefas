package edu.ifpb.ui;

import edu.ifpb.facade.TaskManagementFacade;
import edu.ifpb.state.Task;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class TaskMenu implements Menu {

    private final TaskManagementFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    public TaskMenu(TaskManagementFacade facade) {
        this.facade = facade;
    }

    @Override
    public void show() {
        System.out.println("\n=== ✅ MENU TAREFAS ===");
        System.out.println("1. Criar tarefa");
        System.out.println("2. Listar tarefas");
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
                    List<Task> tasks = facade.listTasks();
                    tasks.forEach(t -> System.out.println("- " + t.getName()));
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
