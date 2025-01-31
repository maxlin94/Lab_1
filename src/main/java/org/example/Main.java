package org.example;

import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        PriceManager.readPricesFromFile();
        menuSelection();
    }

    private static void printMenu() {
        String menuItems = "Elpriser\n========\n1. Inmatning\n2. Min, Max och Medel\n3. Sortera\n4. Bästa Laddningstid (4h)\ne. Avsluta";
        System.out.println(menuItems);
    }

    static void menuSelection() {
        printMenu();
        String input = scanner.next().toLowerCase();
        MenuOption option = MenuOption.from(input.charAt(0));
        if (option != null && input.length() == 1) {
            option.execute();
            if (option != MenuOption.EXIT) {
                menuSelection();
            }
        } else {
            System.out.println("Ogiltigt val");
            menuSelection();
        }
    }
}
