package net.tifoha.utils.algorithms.sort.quick;

import net.tifoha.utils.algorithms.misc.IntPair;
import net.tifoha.utils.algorithms.sort.comparator.IntComparator;
import net.tifoha.utils.algorithms.sort.comparator.LongComparator;
import net.tifoha.utils.algorithms.sort.insertion.InsertionArrayCopy;

import static net.tifoha.utils.algorithms.sort.SortUtils.partition3mirror;


public class Quick3WayMirror {
    private static final int INSERTION_SORT_CUTOFF = 8;

    /**
     * Rearranges the array in ascending order, using the natural order.
     *
     * @param a the array to be sorted
     */
    public static <T extends Comparable<T>> void sort(T[] a) {
        int lo = 0;
        int hi = a.length - 1;
        sort(a, lo, hi);
    }

    public static <T extends Comparable<T>> void sort(T[] a, int lo, int hi) {
        int n = hi - lo + 1;
        if (n < 2) {
            return;
        } else if (n <= INSERTION_SORT_CUTOFF) {
            // cutoff to insertion sort
            InsertionArrayCopy.sort(a, lo, hi);
            return;
        }

        IntPair margin = partition3mirror(a, lo, hi);
        sort(a, lo, margin.getFirst());
        sort(a, margin.getSecond(), hi);
    }

    public static void sort(int[] a, int lo, int hi, IntComparator comparator) {
        int n = hi - lo + 1;
        if (n < 2) {
            return;
        } else if (n <= INSERTION_SORT_CUTOFF) {
            // cutoff to insertion sort
            InsertionArrayCopy.sort(a, lo, hi, comparator);
            return;
        }

        IntPair margin = partition3mirror(a, lo, hi, comparator);
        sort(a, lo, margin.getFirst(), comparator);
        sort(a, margin.getSecond(), hi, comparator);
    }

    public static void sort(long[] a, int lo, int hi, LongComparator comparator) {
        int n = hi - lo + 1;
        if (n < 2) {
            return;
        } else if (n <= INSERTION_SORT_CUTOFF) {
            // cutoff to insertion sort
            InsertionArrayCopy.sort(a, lo, hi, comparator);
            return;
        }

        IntPair margin = partition3mirror(a, lo, hi, comparator);
        sort(a, lo, margin.getFirst(), comparator);
        sort(a, margin.getSecond(), hi, comparator);
    }
}
