package com.krizotto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

public class Day11 {

    private static List<List<Character>> extract2dList(Path path) throws IOException {
        return Files.readAllLines(path)
                    .stream()
                    .map(String::trim)
                    .map(String::chars)
                    .map(stream -> stream.mapToObj(c -> (char) c).collect(Collectors.toList()))
                    .collect(Collectors.toList());
    }

    private static Set<Point> findGalaxies(List<List<Character>> space) {
        Set<Point> galaxies = new HashSet<>();
        IntStream.range(0, space.size())
                 .forEach(y -> IntStream.range(0, space.getFirst().size())
                                        .filter(x -> space.get(y).get(x) == '#')
                                        .forEachOrdered(x -> galaxies.add(new Point(x, y))));
        return galaxies;
    }

    public void solve() throws IOException {
        Path test = Paths.get("src/resources/day11_test.txt");
        Path input = Paths.get("src/resources/day11.txt");
        System.out.println("Day 10");
        System.out.printf("Part A (test): %d%n", solve(test, 2));
        System.out.printf("Part A: %d%n", solve(input, 2));
        System.out.printf("Part B (test): %d%n", solve(test, 100));
        System.out.printf("Part B: %d%n%n", solve(input, 1000000));
    }

    private long solve(Path path, long times) throws IOException {
        List<List<Character>> space = extract2dList(path);
        Set<Point> galaxies = findGalaxies(space);
        List<Integer> blankRows = IntStream.range(0, space.size()).filter(i -> space.get(i).stream().allMatch(character -> character == '.')).boxed().toList();
        List<Integer> blankCols = IntStream.range(0, space.getFirst().size()).filter(i -> space.stream().allMatch(list -> list.get(i) == '.')).boxed().toList();
        return galaxies.stream()
                       .mapToLong(galaxy -> galaxies.stream()
                                                    .filter(g -> !g.equals(galaxy))
                                                    .mapToLong(g -> countDistance(galaxy, g, blankRows, blankCols, times))
                                                    .sum())
                       .sum() / 2;
    }

    private long countDistance(Point p1, Point p2, List<Integer> blankRows, List<Integer> blankCols, long times) {
        int maxY = Math.max(p1.y, p2.y);
        int maxX = Math.max(p1.x, p2.x);
        int minY = Math.min(p1.y, p2.y);
        int minX = Math.min(p1.x, p2.x);
        long addRows = blankRows.stream().filter(i -> i >= minY && i <= maxY).count();
        long addCols = blankCols.stream().filter(i -> i >= minX && i <= maxX).count();
        return Math.abs(maxY - minY) + addRows * (times - 1L) + Math.abs(maxX - minX) + addCols * (times - 1L);
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    static class Point {

        int x;
        int y;
    }

}
