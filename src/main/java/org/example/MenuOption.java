package org.example;

import java.util.Arrays;

enum MenuOption {
    INPUT('1', PriceManager::updatePrices),
    MIN_MAX_AVG('2', PriceManager::minMaxAvgPrice),
    SORT('3', PriceManager::sortPrices),
    BEST_TIME('4', PriceManager::slidingAvgPrice),
    EXIT('e', () -> {
        System.out.println("Avslutar");
        PriceManager.writePricesToFile();
        System.exit(0);
    });

    private final char key;
    private final Runnable action;

    MenuOption(char key, Runnable action) {
        this.key = key;
        this.action = action;
    }

    public void execute() {
        action.run();
    }

    public static MenuOption from(char key) {
        return Arrays.stream(values())
                .filter(option -> option.key == key)
                .findFirst()
                .orElse(null);
    }
}