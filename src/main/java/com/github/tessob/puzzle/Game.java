package com.github.tessob.puzzle;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class Game<P> {

    public static final double delta = 1E-5;

    final List<P> players;
    final double[] bids;

    final public Interval interval;
    final double threshold;

    private int currentPlayer = 0;

    private Game(List<P> players, double from, double to) {
        assert from < to && players.size() > 1;
        this.interval = Interval.of(from, to);
        this.players = players;
        this.threshold = (to - from) / (players.size() + 1) - delta / (players.size() - 1);
        this.bids = new double[players.size()];
    }

    public static <P> Game<P> of(List<P> players, double from, double to) {
        return new Game<>(players, from, to);
    }

    public boolean hasNext() {
        return currentPlayer < players.size();
    }

    public void play(Function<P, Optional<Double>> function) {
        if (currentPlayer >= players.size())
            throw new IllegalStateException();

        Predicate<Double> inInterval = v -> v >= interval.from && v <= interval.to;
        Optional<Double> value = function.apply(players.get(currentPlayer)).filter(inInterval);
        bids[this.currentPlayer] = value.orElseGet(this::applyStrategy);
        currentPlayer++;
    }

    private double applyStrategy() {
        final List<Interval> intervals = interval.split(bids, currentPlayer);
        if (currentPlayer < players.size() - 1)
            return minimizeLoss(intervals);
        else
            return maximizeGain(intervals);
    }

    private double minimizeLoss(List<Interval> intervals) {
        Interval interval = intervals.stream()
                .max(Comparator.comparingDouble(i -> i.weight(2 / 3D)))
                .orElseThrow();

        if (interval.left && interval.right)
            return interval.from + interval.length / 2;

        return interval.left ? interval.to - threshold : interval.from + threshold;
    }

    private double maximizeGain(List<Interval> intervals) {
        Interval interval = intervals.stream()
                .max(Comparator.comparingDouble(i -> i.weight(1D)))
                .orElseThrow();

        if (interval.left == interval.right)
            return interval.from + interval.length / 2;

        return interval.left ? interval.from + delta : interval.to - delta;
    }

    @Override
    public String toString() {
        return Arrays.toString(bids);
    }

}
