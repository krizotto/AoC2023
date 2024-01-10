package com.krizotto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day9 {

    private static List<List<Integer>> extractLists(Path path) throws IOException {
        Pattern pattern = Pattern.compile("(-?\\d+)");
        return Files.readAllLines(path)
                    .stream()
                    .map(pattern::matcher)
                    .map(Matcher::results)
                    .map(stream -> stream.map(MatchResult::group).map(Integer::parseInt).toList())
                    .toList();
    }

    private static Integer extrapolateForward(List<Integer> currentList) {
        List<Integer> newList = IntStream.range(1, currentList.size()).mapToObj(i -> currentList.get(i) - currentList.get(i - 1)).toList();
        return newList.stream().allMatch(integer -> integer.equals(0)) ? currentList.getLast() : (currentList.getLast() + extrapolateForward(newList));
    }

    private static Integer extrapolateBackwards(List<Integer> currentList) {
        List<Integer> newList = IntStream.range(1, currentList.size()).mapToObj(i -> currentList.get(i) - currentList.get(i - 1)).toList();
        return newList.stream().allMatch(integer -> integer.equals(0)) ? currentList.getFirst() : (currentList.getFirst() - extrapolateBackwards(newList));
    }

    public void solve() throws IOException {
        Path test = Paths.get("src/resources/day9_test.txt");
        Path input = Paths.get("src/resources/day9.txt");
        System.out.println("Day 9");
        System.out.printf("Part A (test): %d%n", solveA(test));
        System.out.printf("Part A: %d%n", solveA(input));
        System.out.printf("Part B (test): %d%n", solveB(test));
        System.out.printf("Part B (test): %d%n%n", solveB(input));
    }

    private Integer solveA(Path path) throws IOException {
        return extractLists(path).stream().map(Day9::extrapolateForward).reduce(0, Integer::sum);
    }

    private Integer solveB(Path path) throws IOException {
        return extractLists(path).stream().map(Day9::extrapolateBackwards).reduce(0, Integer::sum);
    }
}
