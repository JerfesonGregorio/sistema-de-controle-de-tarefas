package edu.ifpb;

import edu.ifpb.facade.TaskManagementFacade;
import edu.ifpb.ui.MainMenu;
import edu.ifpb.ui.MenuUI;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        MainMenu mainMenu = new MainMenu();
        MenuUI ui = new MenuUI(mainMenu);
        ui.start();
    }
}
