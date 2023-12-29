package com.krizotto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class Day2 {
    public void solve() throws IOException {
        File testFile = new File("src/resources/day2_test.txt");
        File input = new File("src/resources/day2.txt");
        System.out.println("Day 2");
        System.out.printf("Part A (test): %d\n", solveA(testFile));
        System.out.printf("Part A: %d\n", solveA(input));
        System.out.printf("Part B (test): %d\n", solveB(testFile));
        System.out.printf("Part B: %d\n\n", solveB(input));
    }

    private Integer solveA(File file) throws IOException {
        return extractGames(file).filter(Game::isGameProper).map(Game::getGameId).reduce(0,
                Integer::sum);
    }

    private Integer solveB(File file) throws IOException {
        return extractGames(file).map(Game::findDealPower).reduce(0, Integer::sum);
    }

    private Stream<Game> extractGames(File file) throws IOException {
        return Files.readLines(file, Charsets.UTF_8).stream().map(this::createGame);
    }

    private Game createGame(String line) {
        Game game = new Game();
        String[] number_getDetails = line.split(":");
        // extract game-no: split[0]
        game.setGameId(Integer.parseInt(number_getDetails[0].strip().split(" ")[1]));
        // extract Deals: split[1]
        // split deals
        String[] dealSplit = number_getDetails[1].strip().split(";");
        for (String dealsString : dealSplit) {
            Deal currentDeal = new Deal();
            String[] amount_color = dealsString.strip().split(",");
            for (String dealItem : amount_color) {
                DealItem dItem = new DealItem();
                String[] split2 = dealItem.strip().split(" ");
                dItem.setAmount(Integer.parseInt(split2[0].strip()));
                dItem.setColor(Color.valueOf(split2[1].toUpperCase()));
                currentDeal.getDealItems().add(dItem);
            }
            game.getDeals().add(currentDeal);
        }

        return game;
    }

    @Data
    private class Game {

        private Integer gameId;
        private List<Deal> deals = new ArrayList<>();

        private boolean isGameProper() {
            return deals.stream().allMatch(Deal::isDealProper);
        }

        private Integer findDealPower() {
            List<DealItem> items =
                    deals.stream().map(Deal::getDealItems).flatMap(Collection::stream).toList();
            // red
            Integer reds = items.stream().filter(di -> Color.RED.equals(di.getColor()))
                    .map(DealItem::getAmount).max(Integer::compare).orElse(0);
            // green
            Integer greens = items.stream().filter(di -> Color.GREEN.equals(di.getColor()))
                    .map(DealItem::getAmount).max(Integer::compare).orElse(0);
            // blue
            Integer blues = items.stream().filter(di -> Color.BLUE.equals(di.getColor()))
                    .map(DealItem::getAmount).max(Integer::compare).orElse(0);

            return reds * greens * blues;
        }
    }

    @Data
    private class Deal {
        private List<DealItem> dealItems = new ArrayList<>();

        private boolean isDealProper() {
            return dealItems.stream().allMatch(DealItem::isItemProper);
        }
    }

    @Data
    private class DealItem {
        private Integer amount;
        private Color color;

        private boolean isItemProper() {
            return amount <= color.getMaxAmount();
        }
    }

    @AllArgsConstructor
    @Getter
    private enum Color {
        RED(12), GREEN(13), BLUE(14);

        private Integer maxAmount;
    }
}
