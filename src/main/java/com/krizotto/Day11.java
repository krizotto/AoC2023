package com.krizotto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

public class Day11 {

    private static List<List<Character>> extract2dList(Path path) throws IOException {
        List<List<Character>> linesExtended = Files.readAllLines(path)
                                                   .stream()
                                                   .flatMap(line -> StringUtils.containsOnly(line, '.') ? Stream.of(line, line) : Stream.of(line))
                                                   .map(String::trim)
                                                   .map(String::chars)
                                                   .map(stream -> stream.mapToObj(c -> (char) c).collect(Collectors.toList()))
                                                   .collect(Collectors.toList());
        for (int i = 0; i < linesExtended.getFirst().size(); i++) {
            final int finalI = i;
            if (linesExtended.stream().allMatch(list -> list.get(finalI) == '.')) {
                linesExtended.forEach(line -> line.add(finalI, '.'));
                i += 1;
            }
        }
        return linesExtended;
    }

    public void solve() throws IOException {
        Path test = Paths.get("src/resources/day11_test.txt");
        Path input = Paths.get("src/resources/day11.txt");
        System.out.println("Day 10");
        System.out.printf("Part A (test): %d%n", solveA(test));
        System.out.printf("Part A: %d%n", solveA(input));
        //        System.out.printf("Part B (test): %d%n", solveB(test));
        //        System.out.printf("Part B (test): %d%n%n", solveB(input));
    }

    private Integer solveA(Path path) throws IOException {
        List<List<Character>> space = extract2dList(path);
        Set<Point> galaxies = new HashSet<>();
        for (int y = 0; y < space.size(); y++) {
            for (int x = 0; x < space.get(y).size(); x++) {
                if (space.get(y).get(x) == '#') {
                    galaxies.add(new Point(x, y));
                }
            }
        }
        int sum = 0;
        for (Point galaxy : galaxies) {
            sum += galaxies.stream().filter(g -> !g.equals(galaxy)).map(g -> Math.abs(g.x - galaxy.x) + Math.abs(g.y - galaxy.y)).reduce(0, Integer::sum);
        }

        return sum / 2;
    }

    private Integer solveB(Path path) throws IOException {
        return 0;
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    static class Point {

        int x;
        int y;
    }

}
