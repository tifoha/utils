package net.tifoha.utils.algorithms.sort.insertion;

import net.tifoha.utils.algorithms.sort.comparator.IntComparator;
import net.tifoha.utils.algorithms.sort.comparator.LongComparator;

import java.util.Comparator;

/**
 * The {@code Insertion} class provides static methods for sorting an
 * array using insertion sort.
 * <p>
 * This implementation makes ~ 1/2 n^2 compares and exchanges in
 * the worst case, so it is not suitable for sorting large arbitrary arrays.
 * More precisely, the number of exchanges is exactly equal to the number
 * of inversions. So, for example, it sorts a partially-sorted array
 * in linear time.
 * <p>
 * The sorting algorithm is stable and uses O(1) extra memory.
 *
*/
public class InsertionArrayCopy {

    // This class should not be instantiated.
    private InsertionArrayCopy() {
    }

    /**
     * Rearranges the array in ascending order, using the natural order.
     *
     * @param a the array to be sorted
     */
    public static void sort(Comparable[] a, int lo, int hi) {
        for (int i = lo; i < hi+1; i++) {
            Comparable v = a[i];
            int j = i;
            while (j > 0 && less(v, a[j - 1])) {
                j--;
            }
            if (j < i) {
                System.arraycopy(a, j, a, j + 1, i - j);
                a[j] = v;
            }
        }
    }

//    public static void main(String[] args) {
//        CyclicStopwatch sw = new CyclicStopwatch();
//        Double[] doubles = new Random().doubles(100000, 0, 1)
//                .boxed()
//                .toArray(Double[]::new);
//        sw.start();
//        sort(doubles, 0, doubles.length);
//        sw.stop();
//        System.out.println(sw.getDuration());
//        sw.reset();
//        doubles = new Random().doubles(100000, 0, 1)
//                .boxed()
//                .toArray(Double[]::new);
//        sw.start();
//        sort(doubles, 0, doubles.length);
//        sw.stop();
//        System.out.println(sw.getDuration());
//    }

    /**
     * Rearranges the subarray a[lo..hi) in ascending order, using the natural order.
     *
     * @param a the array to be sorted
     */
    public static void sort(Comparable[] a) {
        sort(a, 0, a.length-1);
    }

    /**
     * Rearranges the array in ascending order, using a comparator.
     *
     * @param a          the array
     * @param comparator the comparator specifying the order
     */
    public static void sort(Object[] a, Comparator comparator) {
        sort(a, 0, a.length-1, comparator);
    }

    /**
     * Rearranges the subarray a[lo..hi) in ascending order, using a comparator.
     *
     * @param a          the array
     * @param lo         left endpoint (inclusive)
     * @param hi         right endpoint (exclusive)
     * @param comparator the comparator specifying the order
     */
    public static void sort(Object[] a, int lo, int hi, Comparator comparator) {
        for (int i = lo; i < hi+1; i++) {
            Object v = a[i];
            int j = i;
            while (j > 0 && less(v, a[j - 1], comparator)) {
                j--;
            }
            if (j < i) {
                System.arraycopy(a, j, a, j + 1, i - j);
                a[j] = v;
            }
        }
    }

    /**
     * Rearranges the subarray a[lo..hi) in ascending order, using a comparator.
     *
     * @param a          the array
     * @param lo         left endpoint (inclusive)
     * @param hi         right endpoint (exclusive)
     * @param comparator the comparator specifying the order
     */
    public static void sort(int[] a, int lo, int hi, IntComparator comparator) {
        for (int i = lo; i < hi+1; i++) {
            int v = a[i];
            int j = i;
            while (j > 0 && less(v, a[j - 1], comparator)) {
                j--;
            }
            if (j < i) {
                System.arraycopy(a, j, a, j + 1, i - j);
                a[j] = v;
            }
        }
    }

    /**
     * Rearranges the subarray a[lo..hi) in ascending order, using a comparator.
     *
     * @param a          the array
     * @param lo         left endpoint (inclusive)
     * @param hi         right endpoint (exclusive)
     * @param comparator the comparator specifying the order
     */
    public static void sort(long[] a, int lo, int hi, LongComparator comparator) {
        for (int i = lo; i < hi + 1; i++) {
            long v = a[i];
            int j = i;
            while (j > 0 && less(v, a[j - 1], comparator)) {
                j--;
            }
            if (j < i) {
                System.arraycopy(a, j, a, j + 1, i - j);
                a[j] = v;
            }
        }
    }


    // return a permutation that gives the elements in a[] in ascending order
    // do not change the original array a[]

    /**
     * Returns a permutation that gives the elements in the array in ascending order.
     *
     * @param a the array
     * @return a permutation {@code p[]} such that {@code a[p[0]]}, {@code a[p[1]]},
     * ..., {@code a[p[n-1]]} are in ascending order
     */
    public static int[] indexSort(Comparable[] a) {
        int n = a.length;
        int[] index = new int[n];
        for (int i = 0; i < n; i++)
            index[i] = i;

        for (int i = 0; i < n; i++)
            for (int j = i; j > 0 && less(a[index[j]], a[index[j - 1]]); j--)
                exch(index, j, j - 1);

        return index;
    }

    /**
     * Returns a permutation that gives the elements in the array in ascending order.
     *
     * @param a the array
     * @return a permutation {@code p[]} such that {@code a[p[0]]}, {@code a[p[1]]},
     * ..., {@code a[p[n-1]]} are in ascending order
     */
    public static int[] indexSort(int[] a) {
        int n = a.length;
        int[] index = new int[n];
        for (int i = 0; i < n; i++)
            index[i] = i;

        for (int i = 0; i < n; i++)
            for (int j = i; j > 0 && less(a[index[j]], a[index[j - 1]]); j--)
                exch(index, j, j - 1);

        return index;
    }

    /**
     * Returns a permutation that gives the elements in the array in ascending order.
     *
     * @param a the array
     * @return a permutation {@code p[]} such that {@code a[p[0]]}, {@code a[p[1]]},
     * ..., {@code a[p[n-1]]} are in ascending order
     */
    public static int[] indexSort(long[] a) {
        int n = a.length;
        int[] index = new int[n];
        for (int i = 0; i < n; i++)
            index[i] = i;

        for (int i = 0; i < n; i++)
            for (int j = i; j > 0 && less(a[index[j]], a[index[j - 1]]); j--)
                exch(index, j, j - 1);

        return index;
    }


    /***************************************************************************
     *  Helper sorting functions.
     ***************************************************************************/

    // is v < w ?
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    // is v < w ?
    private static boolean less(Object v, Object w, Comparator comparator) {
        return comparator.compare(v, w) < 0;
    }

    // is v < w ?
    private static boolean less(int v, int w, IntComparator comparator) {
        return comparator.compare(v, w) < 0;
    }

    // is v < w ?
    private static boolean less(long v, long w, LongComparator comparator) {
        return comparator.compare(v, w) < 0;
    }

    // exchange a[i] and a[j]
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    // exchange a[i] and a[j]  (for indirect sort)
    private static void exch(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }
}