package com.krizotto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class Day2 {

    public void solve() throws IOException {
        Path test = Paths.get("src/resources/day2_test.txt");
        Path input = Paths.get("src/resources/day2.txt");
        System.out.println("Day 2");
        System.out.printf("Part A (test): %d%n", solveA(test));
        System.out.printf("Part A: %d%n", solveA(input));
        System.out.printf("Part B (test): %d%n", solveB(test));
        System.out.printf("Part B: %d%n%n", solveB(input));
    }

    private Integer solveA(Path path) throws IOException {
        return extractGames(path).filter(Game::isGameProper).map(Game::getGameId).reduce(0, Integer::sum);
    }

    private Integer solveB(Path path) throws IOException {
        return extractGames(path).map(Game::findDealPower).reduce(0, Integer::sum);
    }

    private Stream<Game> extractGames(Path path) throws IOException {
        return Files.readAllLines(path, StandardCharsets.UTF_8).stream().map(this::createGame);
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
    private static class Game {

        private Integer gameId;
        private List<Deal> deals = new ArrayList<>();

        private boolean isGameProper() {
            return deals.stream().allMatch(Deal::isDealProper);
        }

        private Integer findDealPower() {
            List<DealItem> items = deals.stream().map(Deal::getDealItems).flatMap(Collection::stream).toList();
            Integer red = items.stream().filter(di -> Color.RED.equals(di.getColor())).map(DealItem::getAmount).max(Integer::compare).orElse(0);
            Integer green = items.stream().filter(di -> Color.GREEN.equals(di.getColor())).map(DealItem::getAmount).max(Integer::compare).orElse(0);
            Integer blue = items.stream().filter(di -> Color.BLUE.equals(di.getColor())).map(DealItem::getAmount).max(Integer::compare).orElse(0);

            return red * green * blue;
        }
    }

    @Data
    private static class Deal {

        private List<DealItem> dealItems = new ArrayList<>();

        private boolean isDealProper() {
            return dealItems.stream().allMatch(DealItem::isItemProper);
        }
    }

    @Data
    private static class DealItem {

        private Integer amount;
        private Color color;

        private boolean isItemProper() {
            return amount <= color.getMaxAmount();
        }
    }

    @AllArgsConstructor
    @Getter
    private enum Color {
        RED(12),
        GREEN(13),
        BLUE(14);

        private final Integer maxAmount;
    }
}
