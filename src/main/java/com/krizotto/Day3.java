package com.krizotto;

import com.google.common.io.Files;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

public class Day3 {
    private static final int ARRAY_SIZE = 140;

    private static String[][] extractArrayFromFile(File file) throws IOException {
        int row = 0;
        int column = 0;
        String[][] returnArray = new String[ARRAY_SIZE][ARRAY_SIZE];
        for (String readLine : Files.readLines(file, Charset.defaultCharset())) {
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
        List<Integer> results = solve(new File("src/resources/day3.txt"));
        System.out.println("Day 3");
        System.out.printf("Part A: %d%n", results.get(0));
        System.out.printf("Part B: %d%n", results.get(1));
        System.out.printf("%n");
    }

    private List<Integer> solve(File file) throws IOException {
        Map<Point, Set<Integer>> gears = new HashMap<>();
        List<Integer> integersToAdd = new ArrayList<>();
        Set<Point> pointsToCheck = new HashSet<>();
        String[][] array = extractArrayFromFile(file);
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
        return List.of(integersToAdd.stream().reduce(0, Integer::sum), gears.values().stream().filter(integers -> integers.size() == 2)
                .map(integers -> integers.stream().reduce(1, (a, b) -> a * b)).reduce(0, Integer::sum));
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
