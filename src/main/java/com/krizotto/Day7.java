package com.krizotto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day7 {

    private static final List<Character> NORMAL_CARD_VALUES = List.of('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A');
    private static final List<Character> BALANCED_CARD_VALUES = List.of('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A');

    private static int compareHands(Hand o1, Hand o2, List<Character> values) {
        if (o1.handType.ordinal() > o2.handType.ordinal()) {
            return 1;
        } else if (o1.handType.ordinal() < o2.handType.ordinal()) {
            return -1;
        } else {
            for (int i = 0; i < o1.cards.size(); i++) {
                if (values.indexOf(o1.cards.get(i)) > values.indexOf(o2.cards.get(i))) {
                    return 1;
                } else if (values.indexOf(o1.cards.get(i)) < values.indexOf(o2.cards.get(i))) {
                    return -1;
                }
            }
            return 0;
        }
    }

    public void solve() throws IOException {
        Path test = Paths.get("src/resources/day7_test.txt");
        Path input = Paths.get("src/resources/day7.txt");

        System.out.println("Day 7");
        System.out.printf("Part A (test): %d%n", solveA(test));
        System.out.printf("Part A: %d%n", solveA(input));
        System.out.printf("Part B (test): %d%n", solveB(test));
        System.out.printf("Part B: %d%n%n", solveB(input));
    }

    private Integer solveA(Path path) throws IOException {
        List<Hand> hands = Files.readAllLines(path, StandardCharsets.UTF_8).stream().map(Hand::new).toList();
        return getTotalWinnings(hands, NORMAL_CARD_VALUES);
    }

    private Integer solveB(Path path) throws IOException {
        List<Hand> hands = Files.readAllLines(path, StandardCharsets.UTF_8).stream().map(Hand::new).toList();
        hands.forEach(Hand::findBestType);
        return getTotalWinnings(hands, BALANCED_CARD_VALUES);
    }

    private Integer getTotalWinnings(List<Hand> hands, List<Character> cardValues) {
        List<Hand> list = hands.stream().sorted((o1, o2) -> compareHands(o1, o2, cardValues)).toList();
        int sum = 0;
        for (int i = 1; i <= list.size(); i++) {
            sum += (list.get(i - 1).bid * i);
        }
        return sum;
    }

    private static class Hand {

        private final List<Character> cards;
        private final Integer bid;
        private Type handType;
        private Map<Character, Long> frequencyMap;

        public Hand(String s) {
            String[] split = s.split(" ");
            this.cards = split[0].chars().mapToObj(c -> (char) c).toList();
            this.bid = Integer.parseInt(split[1]);
            assignType();
        }

        public void assignType() {
            this.frequencyMap = this.cards.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            countBestType();
        }

        public void findBestType() {
            if (this.frequencyMap.containsKey('J')) {
                Optional<Map.Entry<Character, Long>> max = this.frequencyMap.entrySet()
                                                                            .stream()
                                                                            .filter(entry -> !entry.getKey().equals('J'))
                                                                            .max(Map.Entry.comparingByValue());
                if (max.isPresent()) {
                    Long jCount = this.frequencyMap.get('J');
                    Character maxKey = max.get().getKey();
                    Long maxKeyCount = this.frequencyMap.get(maxKey) + jCount;
                    this.frequencyMap.put(maxKey, maxKeyCount);
                    this.frequencyMap.remove('J');
                }
                countBestType();
            }

        }

        private void countBestType() {
            ArrayList<Long> frequencies = new ArrayList<>(this.frequencyMap.values());
            if (frequencies.contains(5L)) {
                this.handType = Type.FIVE;
            } else if (frequencies.contains(4L) && frequencies.contains(1L)) {
                this.handType = Type.FOUR;
            } else if (frequencies.contains(3L) && frequencies.contains(2L)) {
                this.handType = Type.FULL_HOUSE;
            } else if (frequencies.contains(3L)) {
                this.handType = Type.THREE;
            } else if (Collections.frequency(frequencies, 2L) == 2) {
                this.handType = Type.TWO_PAIRS;
            } else if (frequencies.contains(2L)) {
                this.handType = Type.ONE_PAIR;
            } else {
                this.handType = Type.HIGH_CARD;
            }
        }
    }

    private enum Type {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIRS,
        THREE,
        FULL_HOUSE,
        FOUR,
        FIVE
    }

}
