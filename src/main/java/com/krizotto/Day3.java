package com.krizotto;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Day3 {

    private static final int ARRAY_SIZE = 140;

    private static String[][] extractArrayFromFile(Path path) throws IOException {
        int row = 0;
        int column = 0;
        String[][] returnArray = new String[ARRAY_SIZE][ARRAY_SIZE];
        for (String readLine : Files.readAllLines(path, Charset.defaultCharset())) {
            for (char c : readLine.toCharArray()) {
                returnArray[row][column] = String.valueOf(c);
                column++;
            }
            row++;
            column = 0;
        }
        return returnArray;
    }

    public void solve() throws IOException {
        List<Integer> results = solve(Paths.get("src/resources/day3_test.txt"));
        System.out.println("Day 3");
        System.out.printf("Part A (test): %d%n", results.get(0));
        System.out.printf("Part B (test): %d%n", results.get(1));
        results = solve(Paths.get("src/resources/day3.txt"));
        System.out.printf("Part A: %d%n", results.get(0));
        System.out.printf("Part B: %d%n%n", results.get(1));
    }

    private List<Integer> solve(Path path) throws IOException {
        Map<Point, Set<Integer>> gears = new HashMap<>();
        List<Integer> integersToAdd = new ArrayList<>();
        Set<Point> pointsToCheck = new HashSet<>();
        String[][] array = extractArrayFromFile(path);
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < ARRAY_SIZE; row++) {
            for (int col = 0; col < ARRAY_SIZE; col++) {
                String curerntString = array[row][col];
                if (StringUtils.isNumeric(curerntString)) {
                    sb.append(curerntString);
                    pointsToCheck.addAll(getPointsAround(row, col));
                }
                if (!StringUtils.isNumeric(curerntString) && !sb.isEmpty()) {
                    Entry<Point, Integer> gearPoint = findGearPoints(Integer.parseInt(sb.toString()), pointsToCheck, array);
                    Optional.ofNullable(gearPoint).ifPresent(gp -> {
                        Set<Integer> set = Optional.ofNullable(gears.get(gp.getKey())).orElse(new HashSet<>());
                        set.add(gp.getValue());
                        gears.put(gp.getKey(), set);
                    });
                    integersToAdd.add(checkAndAdd(Integer.parseInt(sb.toString()), pointsToCheck, array));
                    pointsToCheck.clear();
                    sb.setLength(0);
                }
            }
        }
        return List.of(integersToAdd.stream().reduce(0, Integer::sum), gears.values()
                                                                            .stream()
                                                                            .filter(integers -> integers.size() == 2)
                                                                            .map(integers -> integers.stream().reduce(1, (a, b) -> a * b))
                                                                            .reduce(0, Integer::sum));
    }

    private List<Point> getPointsAround(int row, int col) {
        List<Point> ret = new ArrayList<>();
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                ret.add(new Point(i, j));
            }
        }
        return ret.stream().filter(point -> point.x >= 0 && point.x < ARRAY_SIZE && point.y >= 0 && point.y < ARRAY_SIZE).toList();
    }

    private int checkAndAdd(int i, Set<Point> pointsToCheck, String[][] array) {
        AtomicBoolean permit = new AtomicBoolean(false);
        pointsToCheck.forEach(p -> {
            String placeInArray = array[p.x][p.y];
            if (!StringUtils.isNumeric(placeInArray) && !".".equals(placeInArray)) {
                permit.set(true);
            }
        });
        return permit.get() ? i : 0;
    }

    private Map.Entry<Point, Integer> findGearPoints(int i, Set<Point> pointsToCheck, String[][] array) {
        AtomicBoolean permit = new AtomicBoolean(false);
        Point thePoint = new Point();
        pointsToCheck.forEach(p -> {
            String placeInArray = array[p.x][p.y];
            if ("*".equals(placeInArray)) {
                permit.set(true);
                thePoint.setX(p.x);
                thePoint.setY(p.y);
            }
        });
        return permit.get() ? Map.entry(thePoint, i) : null;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Point {

        int x;
        int y;
    }
}
