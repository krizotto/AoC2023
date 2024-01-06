package com.krizotto;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class Day5 {

    private static List<BigInteger> stringToNumbers(String str) {
        Pattern pattern = Pattern.compile("(\\d+)");
        return pattern.matcher(str).results().map(MatchResult::group).map(BigInteger::new).collect(Collectors.toList());
    }

    private static BigInteger findSmallest(Iterator<String> iterator, List<BigInteger> seeds, Map<List<BigInteger>, BigInteger> mapping) {
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (StringUtils.isEmpty(next)) {
                swapSeeds(seeds, mapping);
            } else if (StringUtils.contains(next, "map")) {
                mapping.clear();
            } else {
                List<BigInteger> map = stringToNumbers(next);
                mapping.put(List.of(map.get(1), map.get(1).add(map.get(2))), map.get(0).subtract(map.get(1)));
            }
        }
        swapSeeds(seeds, mapping);
        return Collections.min(seeds);
    }

    private static void swapSeeds(List<BigInteger> seeds, Map<List<BigInteger>, BigInteger> mapping) {
        seeds.replaceAll(key -> {
            Optional<Map.Entry<List<BigInteger>, BigInteger>> first = mapping.entrySet().parallelStream().filter(entry -> {
                List<BigInteger> span = entry.getKey();
                return span.getFirst().compareTo(key) <= 0 && span.getLast().compareTo(key) >= 0;
            }).findFirst();
            return first.map(Map.Entry::getValue).map(key::add).orElse(key);
        });
    }

    private static BigInteger mapNumber(Map<List<BigInteger>, BigInteger> mapping, BigInteger number) {
        Optional<Map.Entry<List<BigInteger>, BigInteger>> first = mapping.entrySet().parallelStream().filter(entry -> {
            List<BigInteger> span = entry.getKey();
            return span.getFirst().compareTo(number) <= 0 && span.getLast().compareTo(number) >= 0;
        }).findFirst();
        return first.map(Map.Entry::getValue).map(number::add).orElse(number);
    }

    public void solve() throws IOException {
        Path test = Paths.get("src/resources/day5_test.txt");
        Path input = Paths.get("src/resources/day5.txt");
        System.out.println(solveA(test));
        System.out.println(solveA(input));
        System.out.println(solveB(test)); // wrong answer

    }

    private BigInteger solveA(Path p) throws IOException {
        List<String> lines = Files.readAllLines(p);
        Iterator<String> iterator = lines.iterator();
        List<BigInteger> seeds = stringToNumbers(iterator.next());
        Map<List<BigInteger>, BigInteger> mapping = new HashMap<>();
        return findSmallest(iterator, seeds, mapping);
    }

    private BigInteger solveB(Path p) throws IOException {
        List<String> lines = Files.readAllLines(p);
        Iterator<String> iterator = lines.iterator();
        List<BigInteger> seeds = stringToNumbers(iterator.next());
        List<List<BigInteger>> allSeeds = countSeedRanges(seeds);
        Map<List<BigInteger>, BigInteger> mapping = new HashMap<>();
        return findSmallestB(iterator, allSeeds, mapping);
    }

    private BigInteger findSmallestB(Iterator<String> iterator, List<List<BigInteger>> seeds, Map<List<BigInteger>, BigInteger> mapping) {
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (StringUtils.isEmpty(next)) {
                swapSeedsB(seeds, mapping);
            } else if (StringUtils.contains(next, "map")) {
                mapping.clear();
            } else {
                List<BigInteger> map = stringToNumbers(next);
                mapping.put(List.of(map.get(1), map.get(1).add(map.get(2))), map.get(0).subtract(map.get(1)));
            }
        }
        swapSeedsB(seeds, mapping);
        return Collections.min(seeds.stream().flatMap(Collection::stream).toList());
    }

    private void swapSeedsB(List<List<BigInteger>> seeds, Map<List<BigInteger>, BigInteger> mapping) {
        for (int i = 0; i < seeds.size(); i++) {
            List<BigInteger> range = seeds.get(i);
            // TODO: Count overlapping ranges
            BigInteger mappedStart = mapNumber(mapping, range.getFirst());
            BigInteger mappedEnd = mapNumber(mapping, range.getLast());
            seeds.set(i, Stream.of(mappedStart, mappedEnd).sorted().toList());
        }
    }

    private List<List<BigInteger>> countSeedRanges(List<BigInteger> seeds) {
        List<List<BigInteger>> bigIntegers = new ArrayList<>();
        for (int i = 0; i < seeds.size(); i += 2) {
            BigInteger start = seeds.get(i);
            BigInteger limit = seeds.get(i + 1).add(start);
            bigIntegers.add(List.of(start, limit));
        }
        return bigIntegers;
    }


}