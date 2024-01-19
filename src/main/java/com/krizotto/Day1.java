package com.krizotto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day1 {

    private Integer extractNumbersFromString(String line) {
        StringBuilder sb = new StringBuilder();
        StringBuilder finalNumber = new StringBuilder();
        for (char charAt : line.toCharArray()) {
            if (Character.isDigit(charAt)) {
                sb.append(charAt);
            }
        }
        if (!sb.isEmpty()) {
            finalNumber.append(sb.charAt(0));
            finalNumber.append(sb.charAt(sb.length() - 1));
            return Integer.parseInt(finalNumber.toString());
        } else {
            throw new IllegalStateException();
        }
    }

    private final Map<String, Integer> beginnings = new HashMap<>() {

        {
            put("0", 0);
            put("1", 1);
            put("2", 2);
            put("3", 3);
            put("4", 4);
            put("5", 5);
            put("6", 6);
            put("7", 7);
            put("8", 8);
            put("9", 9);
            put("one", 1);
            put("two", 2);
            put("three", 3);
            put("four", 4);
            put("five", 5);
            put("six", 6);
            put("seven", 7);
            put("eight", 8);
            put("nine", 9);
            put("zero", 0);
        }
    };

    private Integer extractExtendedNumbers(String line) {
        List<Integer> foundNumbers = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            String currentSubstring = line.substring(i);
            beginnings.keySet().stream().filter(currentSubstring::startsWith).findFirst().ifPresent(found -> foundNumbers.add(beginnings.get(found)));
        }
        return 10 * foundNumbers.getFirst() + foundNumbers.getLast();

    }

    private Integer solveA(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        return lines.stream().map(this::extractNumbersFromString).reduce(0, Integer::sum);
    }

    private Integer solveB(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        return lines.stream().map(this::extractExtendedNumbers).reduce(0, Integer::sum);
    }

    public void solve() throws IOException {
        Path test = Paths.get("src/resources/day1_testA.txt");
        Path input = Paths.get("src/resources/day1.txt");
        System.out.println("Day 1");
        System.out.printf("Part A (test): %d%n", solveA(test));
        System.out.printf("Part A: %d%n", solveA(input));
        System.out.printf("Part B (test): %d%n", solveB(test));
        System.out.printf("Part B: %d%n%n", solveB(input));
    }

}
