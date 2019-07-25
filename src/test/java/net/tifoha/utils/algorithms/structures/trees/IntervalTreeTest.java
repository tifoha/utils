package net.tifoha.utils.algorithms.structures.trees;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class IntervalTreeTest {

    @Test
    public void getIntersections_contains() {
        //GIVEN
        List<IntInterval> intervals = Arrays.asList(
                new NormalIntInterval(2, 6),
                new NormalIntInterval(3, 5),
                new NormalIntInterval(3, 4),
                new NormalIntInterval(5, 9)
        );

        //WHEN
        LinkedIntervalTree tree = new LinkedIntervalTree(intervals);

        //THEN
        assertThat(tree.getIntersections(2), hasSize(1));
        assertThat(tree.getIntersections(5), hasSize(3));
        assertThat(tree.getIntersections(4), hasSize(4));
        assertThat(tree.getIntersections(0), empty());
    }
}