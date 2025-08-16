package edu.ifpb.ui;

import java.util.Scanner;

public class MenuUI {

    private final Scanner scanner = new Scanner(System.in);
    private final Menu menu;

    public MenuUI(Menu menu) {
        this.menu = menu;
    }

    public void start() {
        String input;
        do {
            menu.show();
            input = scanner.nextLine();
            menu.handleInput(input);
        } while (!input.equals("0"));
    }
}
