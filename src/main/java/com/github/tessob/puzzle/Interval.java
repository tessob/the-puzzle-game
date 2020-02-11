package com.github.tessob.puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Interval {

    public final double from, to, length;
    public final boolean left, right;

    private Interval(boolean left, double from, double to, boolean right) {
        assert from < to;
        this.left = left;
        this.from = from;
        this.to = to;
        length = to - from;
        this.right = right;
    }

    public static Interval of(double from, double to) {
        return new Interval(false, from, to, false);
    }

    public List<Interval> split(double[] pivots, int current) {
        if (current == 0)
            return List.of(this);

        final int numberOfIntervals = 2 + current - 1;
        List<Interval> intervals = new ArrayList<>(numberOfIntervals);
        intervals.add(new Interval(left, from, pivots[0], !left));
        for (int i = 0; i < numberOfIntervals - 2; i++)
            intervals.add(new Interval(!left, pivots[i], pivots[i + 1], !right));
        intervals.add(new Interval(!right, pivots[current - 1], to, right));
        return intervals;
    }

    public double weight(double k) {
        Function<Boolean, Double> w = v -> v ? 0.25 : 0.5;
        return left != right ?
                length * k :
                (w.apply(left) + w.apply(right)) * length;
    }

    @Override
    public String toString() {
        char start = left ? '[' : '(';
        char end = right ? ']' : ')';
        return String.format("%s%.3f, %.3f%s", start, from, to, end);
    }

}
