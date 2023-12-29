package com.krizotto;

import com.google.common.io.Files;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

public class Day3 {
    public void solve() throws IOException {
        File testFile = new File("src/resources/day3_test.txt");
        File inputFile = new File("src/resources/day3.txt");
        System.out.println("Day 3");
        System.out.printf("Part A (test): %d\n", solveA(testFile, true));
        System.out.printf("Part A: %d\n", solveA(inputFile, false));
        System.out.printf("Part B (test): %d\n", solveB(testFile, true));
        System.out.printf("Part B: %d\n\n", solveB(inputFile, false));
    }

    private Integer solveA(File file, boolean isTest) throws IOException {
        List<Integer> integersToAdd = new ArrayList<>();
        Set<Point> pointsToCheck = new HashSet<>();
        int arraySize = isTest ? 10 : 140;
        String[][] array = extractArrayFromFile(arraySize, file);
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < arraySize; r++) {
            for (int c = 0; c < arraySize; c++) {
                String curerntString = array[r][c];
                if (!StringUtils.isNumeric(curerntString) && !sb.isEmpty()) {
                    integersToAdd.add(
                            checkAndAdd(Integer.parseInt(sb.toString()), pointsToCheck, array));
                    pointsToCheck.clear();
                    sb.setLength(0);
                }

                if (StringUtils.isNumeric(curerntString)) {
                    sb.append(curerntString);
                    pointsToCheck.addAll(getPointsAround(r, c, arraySize));
                }
            }
        }

        return integersToAdd.stream().reduce(0, Integer::sum);
    }

    private List<Point> getPointsAround(int r, int c, int arraySize) {
        List<Point> ret = new ArrayList<>();
        for (int i = r - 1; i < r + 2; i++) {
            for (int j = c - 1; j < c + 2; j++) {
                ret.add(new Point(i, j));
            }
        }
        ret = ret.stream().filter(
                point -> point.x >= 0 && point.x < arraySize && point.y >= 0 && point.y < arraySize)
                .toList();
        return ret;
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

    private Map.Entry<Point, Integer> checkAndAddPart2(int i, Set<Point> pointsToCheck,
            String[][] array) {
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

    private static String[][] extractArrayFromFile(int arraySize, File file) throws IOException {
        int row = 0, column = 0;
        String[][] returnArray = new String[arraySize][arraySize];
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

    private Integer solveB(File file, boolean isTest) throws IOException {
        Map<Point, Set<Integer>> gears = new HashMap<>();
        Set<Point> pointsToCheck = new HashSet<>();
        int arraySize = isTest ? 10 : 140;
        String[][] array = extractArrayFromFile(arraySize, file);
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < arraySize; r++) {
            for (int c = 0; c < arraySize; c++) {
                String curerntString = array[r][c];
                if (!StringUtils.isNumeric(curerntString) && !sb.isEmpty()) {
                    Entry<Point, Integer> gearPoint =
                            checkAndAddPart2(Integer.parseInt(sb.toString()), pointsToCheck, array);
                    Optional.ofNullable(gearPoint).ifPresent(gp -> {
                        Set<Integer> set = gears.get(gp.getKey());
                        if (CollectionUtils.isEmpty(set)) {
                            set = new HashSet<>();
                        }
                        set.add(gp.getValue());
                        gears.put(gp.getKey(), set);
                    });
                    pointsToCheck.clear();
                    sb.setLength(0);
                }

                if (StringUtils.isNumeric(curerntString)) {
                    sb.append(curerntString);
                    pointsToCheck.addAll(getPointsAround(r, c, arraySize));
                }
            }
        }

        return gears.entrySet().stream().filter(entry -> entry.getValue().size() == 2)
                .map(entry -> entry.getValue().stream().reduce(1, (a, b) -> a * b))
                .reduce(0, Integer::sum);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Point {
        int x;
        int y;
    }

}
