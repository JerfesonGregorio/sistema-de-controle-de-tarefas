package edu.ifpb.ui;

import edu.ifpb.facade.TaskManagementFacade;
import edu.ifpb.observer.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserMenu implements Menu {

    private final TaskManagementFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    public UserMenu(TaskManagementFacade facade) {
        this.facade = facade;
    }

    @Override
    public void show() {
        System.out.println("\n=== 👤 MENU USUÁRIOS ===");
        System.out.println("1. Criar usuário");
        System.out.println("2. Listar usuários");
        System.out.println("3. Buscar usuário por nome");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
    }

    @Override
    public void handleInput(String input) {
        try {
            switch (input) {
                case "1":
                    System.out.print("Digite o nome do usuário: ");
                    String nome = scanner.nextLine();
                    facade.createUser(nome);
                    break;
                case "2":
                    List<User> usuarios = facade.listUsers();
                    usuarios.forEach(u -> System.out.println("- " + u.getName()));
                    break;
                case "3":
                    System.out.print("Digite o nome para buscar: ");
                    String name = scanner.nextLine();
                    User user = facade.findUserByName(name);
                    System.out.println(user != null ? "Encontrado: " + user.getName() : "Usuário não encontrado.");
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
