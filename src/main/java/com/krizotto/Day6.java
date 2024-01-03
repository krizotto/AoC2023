package com.krizotto;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day6 {
    public void solve() throws IOException {
        File test = new File("src/resources/day6_test.txt");
        File input = new File("src/resources/day6.txt");
        System.out.println("Day 6");
        System.out.printf("Part A (test): %d%n", solveA(test));
        System.out.printf("Part A : %d%n", solveA(input));
        System.out.printf("Part B (test): %d%n", solveB(test));
        System.out.printf("Part B (test): %d%n", solveB(input));

    }

    private Integer solveA(File input) throws IOException {
        return extractMap(input).entrySet().stream().map(this::countPossibilities).reduce(1, (a, b) -> a * b);
    }

    private BigInteger solveB(File input) throws IOException {
        Map.Entry<BigInteger, BigInteger> timeDistance = extractMapB(input);
        BigInteger maxTime = timeDistance.getKey();
        BigInteger minDistance = timeDistance.getValue();
        return countPossibilities(maxTime, minDistance);
    }

    private Integer countPossibilities(Map.Entry<Integer, Integer> entry) {
        return countPossibilities(BigInteger.valueOf(entry.getKey()), BigInteger.valueOf(entry.getValue())).intValue();
    }

    private BigInteger countPossibilities(BigInteger time, BigInteger distance) {
        BigInteger count = BigInteger.ZERO;
        BigInteger half = time.divide(BigInteger.TWO).add(time.mod(BigInteger.TWO));
        BigInteger current = time.mod(BigInteger.TWO).equals(BigInteger.ONE) ? half : half.add(BigInteger.ONE);
        while (current.multiply(time.subtract(current)).compareTo(distance) > 0) {
            count = count.add(BigInteger.ONE);
            current = current.add(BigInteger.ONE);
        }
        BigInteger doubled = count.multiply(BigInteger.TWO);
        return time.mod(BigInteger.TWO).equals(BigInteger.ONE) ? doubled : doubled.add(BigInteger.ONE);
    }

    Map<Integer, Integer> extractMap(File f) throws IOException {
        List<String> details = Files.readLines(f, StandardCharsets.UTF_8);
        Pattern pattern = Pattern.compile("(\\d+)");
        List<Integer> times = pattern.matcher(details.get(0)).results().map(MatchResult::group).map(Integer::parseInt).toList();
        List<Integer> distances = pattern.matcher(details.get(1)).results().map(MatchResult::group).map(Integer::parseInt).toList();
        return IntStream.range(0, times.size()).boxed().collect(Collectors.toMap(times::get, distances::get));
    }

    Map.Entry<BigInteger, BigInteger> extractMapB(File f) throws IOException {
        List<String> details = Files.readLines(f, StandardCharsets.UTF_8);
        Pattern pattern = Pattern.compile("(\\d+)");
        String time = pattern.matcher(details.get(0)).results().map(MatchResult::group).reduce("", String::concat);
        String distance = pattern.matcher(details.get(1)).results().map(MatchResult::group).reduce("", String::concat);
        return Map.entry(new BigInteger(time), new BigInteger(distance));
    }

}
