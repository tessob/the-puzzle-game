package com.github.tessob.puzzle;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class Application {

    static List<Character> players = List.of('A', 'B', 'C');
    static Game<Character> game = Game.of(players, 0D, 1D);

    public static void main(String[] args) {
        System.out.println(String.format(
                "[type a number within the interval to play it, or anything else to play automatically]\n\r" +
                        "Game on interval from %.2f to %.2f", game.interval.from, game.interval.to
        ));
        while (game.hasNext())
            game.play(Application::getNumber);
        System.out.println(game);
    }

    private static Optional<Double> getNumber(Character player) {
        System.out.println(String.format(
                "Player %s?", player.toString()
        ));
        try {
            return Optional.of(readInput());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    private static double readInput() throws NoSuchElementException {
        Scanner input = new Scanner(System.in);
        return input.nextDouble();
    }

}
