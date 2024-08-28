package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.IntStream;

public class PriceManager {
    private final static List<PricePerHour> prices = new ArrayList<>();
    private static final String FILE_PATH = "src/main/resources/prices.txt";

    static void readPricesFromFile() {
        try {
            Scanner scanner = new Scanner(new File(FILE_PATH));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(", ");
                String hour = tokens[0];
                try {
                    int price = Integer.parseInt(tokens[1]);
                    prices.add(new PricePerHour(hour, price));
                } catch (NumberFormatException e) {
                    System.out.println("Ogiltigt pris: " + tokens[1]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println((e.getMessage()));
        }
    }

    static void writePricesToFile() throws IOException {
        Files.createDirectories(Paths.get("src/main/resources"));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            for (PricePerHour price : prices) {
                writer.write(price.hours() + ", " + price.price());
                writer.newLine();
            }
            System.out.println("Priser sparade till fil");
        } catch (IOException e) {
            System.out.println("Fel när priser skulle sparas: " + e.getMessage());
        }
    }

    static void updatePrices() {
        prices.clear();
        Scanner scanner = new Scanner(System.in);
        String[] hours = IntStream
                .range(0, 24)
                .mapToObj(i -> String.format("%02d-%02d", i, (i + 1) % 24))
                .toArray(String[]::new);
        for (int i = 0; i < hours.length; i++) {
            String hour = hours[i];
            System.out.println(hour);
            try {
                int price = scanner.nextInt();
                prices.add(new PricePerHour(hour, price));
            } catch (NumberFormatException e) {
                System.out.println("Ogiltigt pris");
                scanner.nextLine();
                i--;
            }
        }
    }

    static void sortPrices() {
        if (prices.isEmpty()) {
            System.out.println("Inga priser inlagda");
            return;
        }
        List<PricePerHour> sortByPrice = new ArrayList<>(prices);
        sortByPrice.sort(Comparator.comparingInt(PricePerHour::price));
        for (PricePerHour p : sortByPrice) {
            System.out.println(p.hours() + " " + p.price() + " öre");
        }
    }

    static void minMaxAvgPrice() {
        if (prices.isEmpty()) {
            System.out.println("Inga priser inlagda");
            return;
        }
        IntSummaryStatistics stats = prices.stream().mapToInt(PricePerHour::price).summaryStatistics();
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("Min: " + stats.getMin() + " öre");
        System.out.println("Max: " + stats.getMax() + " öre");
        System.out.println("Snitt: " + df.format(stats.getAverage()) + " öre");
    }

    static void slidingAvgPrice() {
        if (prices.isEmpty()) {
            System.out.println("Inga priser inlagda");
            return;
        }
        double lowestAvg = Double.MAX_VALUE;
        double currentSum = 0;
        int startIndex = 0;
        int windowSize = 4;
        for (int i = 0; i < prices.size(); i++) {
            if (i >= windowSize) {
                if (currentSum / windowSize < lowestAvg) {
                    lowestAvg = currentSum / windowSize;
                }
                currentSum -= prices.get(i - windowSize).price();
            }
            currentSum += prices.get(i).price();
        }
        String startTime = prices.get(startIndex).hours().split("-")[0];
        System.out.println("Börja ladda klockan " + startTime + ":00");
        System.out.println("Snittpris: " + lowestAvg + " öre");
    }
}
