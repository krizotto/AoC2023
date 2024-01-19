package com.krizotto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class Day10 {

    public void solve() throws IOException {
        Path test = Paths.get("src/resources/day10_test.txt");
        Path input = Paths.get("src/resources/day10.txt");
        System.out.println("Day 10");
        System.out.printf("Part A (test): %d%n", solveA(test, Direction.S));
        System.out.printf("Part A: %d%n", solveA(input, Direction.E));
        System.out.printf("Part B (test): %d%n", solveB(test, Direction.S));
        System.out.printf("Part B (test): %d%n%n", solveB(input, Direction.E));
    }

    private long solveA(Path path, Direction direction) throws IOException {
        Arrow arrow = findStart(processStrings(Files.readAllLines(path)), direction);
        arrow.findLoop();
        return arrow.steps / 2;
    }

    private long solveB(Path path, Direction direction) throws IOException {
        Arrow arrow = findStart(processStrings(Files.readAllLines(path)), direction);
        arrow.findLoop();

        // go through all the lines and calculate point in polygon algorithm extended
        return CollectionUtils.intersection(verticalInsides, horizontalInsides).size();
    }

    private Arrow findStart(char[][] input, Direction direction) {
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input.length; j++) {
                if (input[i][j] == 'S') {
                    return new Arrow(i, j, direction, input); // assume direction for test data
                }
            }
        }
        throw new RuntimeException("Could not find S");
    }

    private char[][] processStrings(List<String> strings) {
        int length = strings.getFirst().length();
        char[][] chars = new char[length][length];
        for (int i = 0; i < strings.size(); i++) {
            char[] line = strings.get(i).toCharArray();
            System.arraycopy(line, 0, chars[i], 0, line.length);
        }
        return chars;
    }

    enum Direction {
        N,
        E,
        S,
        W
    }

    @AllArgsConstructor
    @NoArgsConstructor
    static class Arrow {

        int x;
        int y;
        Direction direction;
        char current = 'S';
        long steps = 0;
        char[][] chars;
        Set<Arrow> loop = new HashSet<>();
        public Arrow(int x, int y, Direction direction, char[][] chars) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.chars = chars;
        }
        public Arrow(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Arrow arrow = (Arrow) o;
            return x == arrow.x && y == arrow.y;
        }

        void move() {
            this.steps += 1;
            switch (direction) {
                case E -> this.y += 1;
                case N -> this.x -= 1;
                case S -> this.x += 1;
                case W -> this.y -= 1;
                default -> throw new RuntimeException("Wrong direction");
            }
        }

        void setDirection() {
            current = chars[x][y];
            switch (current) {
                case '-' -> {
                    if (List.of(Direction.N, Direction.S).contains(direction)) {
                        throw new RuntimeException("Bad way");
                    }
                }
                case '|' -> {
                    if (List.of(Direction.E, Direction.W).contains(direction)) {
                        throw new RuntimeException("Bad way");
                    }
                }
                case 'L' -> {
                    switch (direction) {
                        case S -> direction = Direction.E;
                        case W -> direction = Direction.N;
                        default -> throw new RuntimeException("Bad way L, x=" + x + ", y=" + y);
                    }

                }
                case 'J' -> {
                    switch (direction) {
                        case S -> direction = Direction.W;
                        case E -> direction = Direction.N;
                        default -> throw new PipeException();
                    }
                }
                case '7' -> {
                    switch (direction) {
                        case N -> direction = Direction.W;
                        case E -> direction = Direction.S;
                        default -> throw new PipeException();
                    }
                }
                case 'F' -> {
                    switch (direction) {
                        case N -> direction = Direction.E;
                        case W -> direction = Direction.S;
                        default -> throw new PipeException();
                    }
                }
                case '.' -> throw new PipeException();
                case 'S' -> System.out.println("purrfect");
                default -> System.out.println("Wrong sign");
            }
        }

        private void findLoop() {
            do {
                move();
                loop.add(new Arrow(x, y));
                setDirection();
            } while (chars[x][y] != 'S');
        }

        private boolean isPartOfLoop(int x, int y) {
            return this.loop.stream().anyMatch(arrow -> arrow.x == x && arrow.y == y);
        }

        class PipeException extends RuntimeException {

            public PipeException() {
                super("Bad way! " + current + ", x = " + x + ", y = " + y);
            }
        }
    }

}
