package com.krizotto;

import com.google.common.io.Files;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 {
    public void solve() throws IOException {
        File testFile = new File("src/resources/day4_test.txt");
        File input = new File("src/resources/day4.txt");
        List<Card> testCards = Files.readLines(testFile, StandardCharsets.UTF_8).stream().map(Card::extract).toList();
        List<Card> inputCards = Files.readLines(input, StandardCharsets.UTF_8).stream().map(Card::extract).toList();
        System.out.println("Day 4");
        System.out.printf("Part A (test): %d%n", solveA(testCards));
        System.out.printf("Part A: %d%n", solveA(inputCards));
        System.out.printf("Part B (test): %d%n", solveB(testCards));
        System.out.printf("Part B : %d%n", solveB(inputCards));

    }

    private Integer solveA(List<Card> cards) {
        return cards.stream().map(Card::countPoints).reduce(0, Integer::sum);
    }

    private Integer solveB(List<Card> cards) {
        Map<Integer, Integer> cardCount = new HashMap<>();
        for (int i = 1; i <= cards.size(); i++) {
            cardCount.merge(i, 1, Integer::sum);
            for (int j = 0; j < cardCount.get(i); j++) {
                int matches = cards.get(i - 1).matchCount;
                for (int k = i + 1; k < i + 1 + matches; k++) {
                    cardCount.merge(k, 1, Integer::sum);
                }
            }
        }
        return cardCount.values().stream().reduce(0, Integer::sum);
    }

    @Data
    @NoArgsConstructor
    private static class Card {
        private Integer number;
        private List<Integer> winningNumbers = new ArrayList<>();
        private List<Integer> myNumbers = new ArrayList<>();
        private Integer matchCount;

        public static Card extract(String s) {
            Card card = new Card();
            String[] split = s.split(":");
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(split[0]);
            card.number = matcher.find() ? Integer.parseInt(matcher.group()) : null;
            split = split[1].split("\\|");
            pattern = Pattern.compile("(\\d+)");
            card.winningNumbers = pattern.matcher(split[0]).results().map(MatchResult::group).map(Integer::parseInt).toList();
            card.myNumbers = pattern.matcher(split[1]).results().map(MatchResult::group).map(Integer::parseInt).toList();
            card.matchCount = CollectionUtils.intersection(card.winningNumbers, card.myNumbers).size();
            return card;
        }

        private Integer countPoints() {
            return this.matchCount == 0 ? 0 : (int) Math.pow(2, (double) this.matchCount - 1);
        }
    }
}
