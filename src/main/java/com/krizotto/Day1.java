package com.krizotto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Day1 {
    private Integer extractNumbersFromString(String line) {
        StringBuilder sb = new StringBuilder();
        StringBuilder finalNumber = new StringBuilder();
        for (char charAt : line.toCharArray()) {
            if (Character.isDigit(charAt)) {
                sb.append(charAt);
            }
        }
        if (sb.length() > 0) {
            finalNumber.append(sb.charAt(0));
            finalNumber.append(sb.charAt(sb.length() - 1));
            return Integer.parseInt(finalNumber.toString());
        } else
            throw new IllegalStateException();
    }

    private Map<String, Integer> beginnings = new HashMap<String, Integer>() {
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

        List<Integer> foundSubstrings = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            String currentSubstring = line.substring(i, line.length());
            String foundBeginning = beginnings.keySet().stream().filter(b -> currentSubstring.startsWith(b)).findFirst()
                    .orElse(null);
            if (foundBeginning != null) {
                foundSubstrings.add(beginnings.get(foundBeginning));
            }
        }
        return 10 * foundSubstrings.get(0) + foundSubstrings.get(foundSubstrings.size() - 1);

    }

    private Integer solveA(File file) throws IOException {
        List<String> lines = Files.readLines(file, Charsets.UTF_8);
        return lines.stream().map(this::extractNumbersFromString).reduce(0, Integer::sum);
    }

    private Integer solveB(File file) throws IOException {
        List<String> lines = Files.readLines(file, Charsets.UTF_8);
        return lines.stream().map(this::extractExtendedNumbers).reduce(0, Integer::sum);
    }

    private Integer solveATest() throws IOException {
        File part1Test = new File("src/resources/day1_testA.txt");
        return solveA(part1Test);
    }

    private Integer solveA() throws IOException {
        File part1 = new File("src/resources/day1.txt");
        return solveA(part1);
    }

    private Integer solveBTest() throws IOException {
        File part2Test = new File("src/resources/day1_testB.txt");
        return solveB(part2Test);
    }

    private Integer solveB() throws IOException {
        File part2 = new File("src/resources/day1.txt");
        return solveB(part2);
    }

    public void solve() throws IOException {
        System.out.println("Day 1");
        System.out.printf("Part A (test): %d\n", solveATest());
        System.out.printf("Part A: %d\n", solveA());
        System.out.printf("Part B (test): %d\n", solveBTest());
        System.out.printf("Part B: %d\n", solveB());
    }

}
