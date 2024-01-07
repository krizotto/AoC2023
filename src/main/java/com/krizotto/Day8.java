package com.krizotto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day8 {

    public void solve() throws IOException {
        Path test = Paths.get("src/resources/day8_test.txt");
        Path input = Paths.get("src/resources/day8.txt");

        System.out.println("Day 8");
        System.out.printf("Part A: %d%n", solveA(input));
        System.out.printf("Part B: %d%n", solveB(input));
    }

    private Integer solveA(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        Iterator<String> iterator = lines.iterator();
        List<Character> commands = iterator.next().chars().mapToObj(c -> (char) c).toList();
        iterator.next(); // skip empty line
        Pattern pattern = Pattern.compile("([A-Z]{3})");
        Map<String, List<String>> map = new HashMap<>();
        while (iterator.hasNext()) {
            List<String> list = pattern.matcher(iterator.next()).results().map(MatchResult::group).toList();
            map.put(list.getFirst(), list.subList(1, 3));
        }
        int totalSteps = 0;
        String currentKey = "AAA";
        while (!currentKey.equals("ZZZ")) {
            Character c = commands.get(totalSteps % commands.size());
            currentKey = map.get(currentKey).get(c == 'L' ? 0 : 1);
            totalSteps += 1;
        }

        return totalSteps;
    }

    private long solveB(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        Iterator<String> iterator = lines.iterator();
        char[] commands = iterator.next().toCharArray();
        iterator.next(); // skip empty line
        Pattern pattern = Pattern.compile("([A-Z0-9]{3})");
        Map<String, List<String>> map = new HashMap<>();
        while (iterator.hasNext()) {
            List<String> list = pattern.matcher(iterator.next()).results().map(MatchResult::group).toList();
            map.put(list.getFirst(), list.subList(1, 3));
        }

        return map.keySet().stream().filter(s -> s.endsWith("A")).map(s -> {
            String current = s;
            long totalSteps = 0;
            while (!current.endsWith("Z")) {
                int c = commands[(int) (totalSteps) % commands.length] == 'L' ? 0 : 1;
                current = map.get(current).get(c);
                totalSteps += 1;
            }
            return totalSteps;
        }).reduce(1L, (a, b) -> (a * b) / gcd(a, b));

    }

    private long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}

