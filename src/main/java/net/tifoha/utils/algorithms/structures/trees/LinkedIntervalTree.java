package net.tifoha.utils.algorithms.structures.trees;

import java.util.*;

import static java.util.Collections.binarySearch;

public class LinkedIntervalTree implements Iterable<IntInterval> {
    public static final Comparator<IntInterval> LEFT_COMPARATOR = Comparator.comparingInt(IntInterval::getLeft);
    public static final Comparator<IntInterval> RIGHT_COMPARATOR = Comparator.comparingInt(IntInterval::getRight).reversed();
    private final LinkedIntervalTree parent;
    private final LinkedIntervalTree left;
    private final LinkedIntervalTree right;
    private final int median;
    private final List<IntInterval> leftIntervals = new ArrayList<>();
    private final List<IntInterval> rightIntervals = new ArrayList<>();

    public LinkedIntervalTree(List<IntInterval> intervals) {
        this(null, intervals);
    }

    private LinkedIntervalTree(LinkedIntervalTree parent, List<IntInterval> intervals) {
        this.parent = parent;
        List<IntInterval> toLeft = new LinkedList<>();
        List<IntInterval> toRight = new LinkedList<>();

        IntInterval first = intervals.get(0);
        int min = first.getLeft();
        int max = first.getRight();
        for (IntInterval interval : intervals) {
            min = min > interval.getLeft() ? interval.getLeft() : min;
            max = max < interval.getRight() ? interval.getRight() : max;
        }


        median = (min + max) / 2;

        for (IntInterval interval : intervals) {
            if (interval.getRight() < median) {
                toLeft.add(interval);
            } else if (interval.getLeft() > median) {
                toRight.add(interval);
            } else {
                leftIntervals.add(interval);
                rightIntervals.add(interval);
            }
        }

        leftIntervals.sort(LEFT_COMPARATOR);
        rightIntervals.sort(RIGHT_COMPARATOR);

        left = toLeft.isEmpty() ? null : new LinkedIntervalTree(this, toLeft);
        right = toRight.isEmpty() ? null : new LinkedIntervalTree(this, toRight);
    }


    @Override
    public Iterator<IntInterval> iterator() {
        Iterator<IntInterval> result = leftIntervals.iterator();

        if (left != null) {
            result = concat(left.iterator(), result);
        }

        if (right != null) {
            result = concat(result, right.iterator());
        }

        return result;
    }

    public LinkedIntervalTree getLeft() {
        return left;
    }

    public LinkedIntervalTree getRight() {
        return right;
    }

    public int getMedian() {
        return median;
    }

    public List<IntInterval> getIntersections(int value) {
        List<IntInterval> results = new LinkedList<>();

        if (value < median) {
            if (left != null) {
                results.addAll(left.getIntersections(value));
            }
            addLeftIntervalIntersections(value, results);
        } else if (value > median) {
            addRightIntervalIntersections(value, results);
            if (right != null) {
                results.addAll(right.getIntersections(value));
            }
        } else {
            addLeftIntervalIntersections(value, results);
        }

        return results;
    }

    private void addRightIntervalIntersections(int value, List<IntInterval> results) {
        if (rightIntervals.isEmpty() || value > rightIntervals.get(0).getRight()) {
            return;
        }

        int index = binarySearch(rightIntervals, new SingleIntInterval(value), RIGHT_COMPARATOR);
        if (index >= 0) {
            results.addAll(rightIntervals.subList(0, index + 1));
        } else {
            results.addAll(rightIntervals.subList(0, -index));
        }
    }

    private void addLeftIntervalIntersections(int value, List<IntInterval> results) {
        if (leftIntervals.isEmpty() || value < leftIntervals.get(0).getLeft()) {
            return;
        }

        int index = binarySearch(leftIntervals, new SingleIntInterval(value), LEFT_COMPARATOR);
        if (index >= 0) {
            results.addAll(leftIntervals.subList(0, index + 1));
        } else {
            results.addAll(leftIntervals.subList(0, -index));
        }
    }

    private static <T> Iterator<T> concat(Iterator<T> first, Iterator<T> second) {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return first.hasNext() && second.hasNext();
            }

            @Override
            public T next() {
                if (first.hasNext()) {
                    return first.next();
                }
                if (second.hasNext()) {
                    return second.next();
                }

                throw new NoSuchElementException();
            }
        };
    }

    public static void main(String[] args) {
        List<IntInterval> intervals = Arrays.asList(
                new NormalIntInterval(1, 3),
                new NormalIntInterval(2, 3),
                new NormalIntInterval(5, 7)
        );

        LinkedIntervalTree tree = new LinkedIntervalTree(intervals);
        System.out.println(tree.getIntersections(2));
        System.out.println(tree.getIntersections(4));
    }
}
