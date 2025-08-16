package edu.ifpb.ui;

import edu.ifpb.facade.TaskManagementFacade;

import java.sql.SQLException;

public class SystemMenu implements Menu{

    private final TaskManagementFacade facade;

    public SystemMenu(TaskManagementFacade facade) {
        this.facade = facade;
    }

    @Override
    public void show() {
        System.out.println("\n=== ⚙️ MENU SISTEMA ===");
        System.out.println("1. Testar conexão com banco");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
    }

    @Override
    public void handleInput(String input) {
        try {
            switch (input) {
                case "1":
                    facade.pingDatabase();
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
