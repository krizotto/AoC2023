package com.krizotto;

import com.google.common.io.Files;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Day7 {
    public void solve() throws IOException {
        File test = new File("src/resources/day7_test.txt");
        File input = new File("src/resources/day7.txt");
        System.out.println("Day 7");
        System.out.printf("Part A (test): %d%n", solveA(test));
//        System.out.printf("Part A : %d%n", solveA(input));
//        System.out.printf("Part B (test): %d%n", solveB(test));
//        System.out.printf("Part B (test): %d%n", solveB(input));

    }

    private Integer solveA(File f) throws IOException {
        List<Hand> hands = Files.readLines(f, StandardCharsets.UTF_8).stream().map(Hand::new).toList();
        hands.forEach(Hand::assignType);

        return 0;
    }

    @Data
    private static class Hand {
        private List<Character> cards;
        private Integer bid;
        private Type handType;

        public Hand(String s) {
            String[] split = s.split(" ");
            this.cards = split[0].chars().mapToObj(c -> (char) c).toList();
            this.bid = Integer.parseInt(split[1]);
        }

        public void assignType() {
            Pattern five = Pattern.compile("^(.)\\1*$");
//            five.matcher()
//            switch (this.cards) {
//                case
//            }


            this.handType = Type.HIGH_CARD;
        }
    }

    private enum Type {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE,
        FULL_HOUSE,
        FOUR,
        FIVE
    }

    private static final List<Character> cardValues = List.of('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A');
}
