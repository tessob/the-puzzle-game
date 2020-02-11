package com.github.tessob.puzzle;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntervalTest {

    @Test
    void length() {
        Interval interval = Interval.of(3D, 5D);
        assertEquals(2D, interval.length, 0.000001);
    }

    @Test
    void split_1() {
        Interval interval = Interval.of(1d, 5D);
        double[] pivots = {2D, 3D, 4D};
        List<Interval> intervals = interval.split(pivots, 2);

        for (var i : intervals)
            System.out.print(i);

        assertEquals(4, intervals.size());
    }

    @Test
    void split_2() {
        Interval interval = Interval.of(0d, 1D);
        double[] pivots = {0.7, 0D, 0D};
        List<Interval> intervals = interval.split(pivots, 1);

        for (var i : intervals)
            System.out.print(i);

        assertEquals(0D, intervals.get(0).from, 0.001);
        assertEquals(0.7, intervals.get(0).to, 0.001);
        assertFalse(intervals.get(0).left);
        assertTrue(intervals.get(0).right);

        assertEquals(0.7, intervals.get(1).from, 0.001);
        assertEquals(1D, intervals.get(1).to, 0.001);
        assertTrue(intervals.get(1).left);
        assertFalse(intervals.get(1).right);
    }

    @Test
    void of() {
        Interval interval = Interval.of(2D, 3D);
        assertFalse(interval.left);
        assertFalse(interval.right);
    }

}
