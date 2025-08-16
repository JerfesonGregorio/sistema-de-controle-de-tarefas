package edu.ifpb.ui;

import edu.ifpb.facade.TaskManagementFacade;

import java.sql.SQLException;
import java.util.Scanner;

public class MainMenu implements Menu {

    private final TaskManagementFacade facade = new TaskManagementFacade();

    public MainMenu() throws SQLException {

    }

    @Override
    public void show() {
        System.out.println("\n=== 📋 MENU PRINCIPAL ===");
        System.out.println("1. Usuários");
        System.out.println("2. Tarefas");
        System.out.println("3. Alocar tarefas");
        System.out.println("4. Sistema");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    @Override
    public void handleInput(String input) {
        MenuUI menuUI;
        switch (input) {
            case "1":
                menuUI = new MenuUI(new UserMenu(facade));
                menuUI.start();
                break;
            case "2":
                menuUI = new MenuUI(new TaskMenu(facade));
                menuUI.start();
                break;
            case "3":
                menuUI = new MenuUI(new RelationMenu(facade));
                menuUI.start();
                break;
            case "4":
                menuUI = new MenuUI(new SystemMenu(facade));
                menuUI.start();
                break;
            case "0":
                System.out.println("Saindo...");
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }
}
