package net.tifoha.utils.algorithms.sort;

import net.tifoha.utils.algorithms.sort.comparator.IntComparator;
import net.tifoha.utils.algorithms.sort.comparator.LongComparator;
import net.tifoha.utils.misc.IntPair;

import java.util.Comparator;

import static java.lang.Math.max;
import static java.util.Comparator.naturalOrder;

public class SortUtils {
    private static final int INSERTION_SORT_CUTOFF = 8;
    private static final int MEDIAN_OF_3_CUTOFF = 32;

    /***************************************************************************
     *  Helper sorting functions.
     ***************************************************************************/

    /**
     * is a < b ?
     */
    public static <T extends Comparable<T>> boolean less(T a, T b) {
        return less(a, b, naturalOrder());
    }

    /**
     * is a < b ?
     */
    public static <T> boolean less(T a, T b, Comparator<T> comparator) {
        return comparator.compare(a, b) < 0;
    }

    /**
     * is a < b ?
     */
    public static boolean less(int a, int b, IntComparator comparator) {
        return comparator.compare(a, b) < 0;
    }

    /**
     * is a < b ?
     */
    public static boolean less(long a, long b, LongComparator comparator) {
        return comparator.compare(a, b) < 0;
    }

    /**
     * is a < b ?
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean less(int a, int b) {
        return a < b;
    }

    /**
     * is a > b ?
     */
    public static <T> boolean more(T a, T b, Comparator<T> comparator) {
        return comparator.compare(a, b) > 0;
    }

    /**
     * is a > b ?
     */
    public static boolean more(int a, int b, IntComparator comparator) {
        return comparator.compare(a, b) > 0;
    }

    /**
     * is a > b ?
     */
    public static boolean more(long a, long b, LongComparator comparator) {
        return comparator.compare(a, b) > 0;
    }

    // exchange a[i] and a[j]
    public static void exch(Object[] a, int i, int j) {
        if (i == j) {
            return;
        }
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    // exchange a[i] and a[j]
    public static void exch(int[] a, int i, int j) {
        if (i == j) {
            return;
        }
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    // exchange a[i] and a[j]
    public static void exch(long[] a, int i, int j) {
        if (i == j) {
            return;
        }
        long swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> void merge(T[] a, T[] aux, int lo, int mid, int hi) {
        if (isMerged(a, lo, mid, hi)) {
            return;
        }
        System.arraycopy(a, lo, aux, lo, hi - lo + 1);
        int i = lo;
        int j = mid + 1;

        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > hi) {
                a[k] = aux[i++];
            } else if (less(aux[j], aux[i])) {
                a[k] = aux[j++];
            } else {
                a[k] = aux[i++];
            }
        }
    }

    private static <T extends Comparable<T>> boolean isMerged(T[] a, int lo, int mid, int hi) {
        if (lo == hi) {
            return true;
        } else if (hi - lo == 1) {
            if (less(a[hi], a[lo])) {
                exch(a, hi, lo);
            }
            return true;
//        } else if (less(a[mid], a[mid + 1])) {
//            return true;
        }

        return false;
    }

    public static <T extends Comparable<T>> boolean isSorted(T[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    public static <T extends Comparable<T>> boolean isSorted(T[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (less((T) a[i], (T) a[i - 1])) return false;
        return true;
    }

    public static <T extends Comparable<T>> int partition(Comparable[] a, int lo, int hi) {
        Comparable v = a[lo];
        int i = lo;
        int j = hi + 1;
        while (i < j) {
            while (i < hi) if (less(v, a[++i])) break;
            while (j > lo) if (less(a[--j], v)) break;
            if (i >= j) {
                break;
            }
            exch(a, i, j);
        }
        exch(a, lo, j);
        return j;
    }

    public static <T extends Comparable<T>> IntPair partition3(T[] a, int lo, int hi) {
        T v = a[lo];
        int lt = lo;
        int i = lo + 1;
        int gt = hi;
        while (i <= gt) {
            int cmp = compare(v, a[i]);
            if (cmp > 0) {
                exch(a, lt++, i++);
            } else if (cmp < 0) {
                exch(a, i, gt--);
            } else {
                i++;
            }
        }
        return IntPair.simple(lt - 1, gt + 1);
    }

    public static <T extends Comparable<T>> IntPair partition3mirror(T[] a, int lo, int hi) {
        int m = getMedianIndex(a, lo, hi);
        exch(a, m, lo);

        // Bentley-McIlroy 3-way partitioning
        int i = lo, j = hi + 1;
        int p = lo, q = hi + 1;
        T v = a[lo];
        while (true) {
            while (less(a[++i], v))
                if (i == hi) break;
            while (less(v, a[--j]))
                if (j == lo) break;

            // pointers cross
            if (i == j && eq(a[i], v))
                exch(a, ++p, i);
            if (i >= j) break;

            exch(a, i, j);
            if (eq(a[i], v)) exch(a, ++p, i);
            if (eq(a[j], v)) exch(a, --q, j);
        }


        i = j + 1;
        for (int k = lo; k <= p; k++)
            exch(a, k, j--);
        for (int k = hi; k >= q; k--)
            exch(a, k, i++);

        return IntPair.simple(j, i);
    }

    public static IntPair partition3mirror(int[] a, int lo, int hi, IntComparator comparator) {
        int m = getMedianIndex(a, lo, hi, comparator);
        exch(a, m, lo);

        // Bentley-McIlroy 3-way partitioning
        int i = lo, j = hi + 1;
        int p = lo, q = hi + 1;
        int v = a[lo];
        while (true) {
            while (less(a[++i], v, comparator))
                if (i == hi) break;
            while (less(v, a[--j], comparator))
                if (j == lo) break;

            // pointers cross
            if (i == j && eq(a[i], v, comparator))
                exch(a, ++p, i);
            if (i >= j) break;

            exch(a, i, j);
            if (eq(a[i], v, comparator)) exch(a, ++p, i);
            if (eq(a[j], v, comparator)) exch(a, --q, j);
        }


        i = j + 1;
        for (int k = lo; k <= p; k++)
            exch(a, k, j--);
        for (int k = hi; k >= q; k--)
            exch(a, k, i++);

        return IntPair.simple(j, i);
    }

    public static IntPair partition3mirror(long[] a, int lo, int hi, LongComparator comparator) {
        int m = getMedianIndex(a, lo, hi, comparator);
        exch(a, m, lo);

        // Bentley-McIlroy 3-way partitioning
        int i = lo, j = hi + 1;
        int p = lo, q = hi + 1;
        long v = a[lo];
        while (true) {
            while (less(a[++i], v, comparator))
                if (i == hi) break;
            while (less(v, a[--j], comparator))
                if (j == lo) break;

            // pointers cross
            if (i == j && eq(a[i], v, comparator))
                exch(a, ++p, i);
            if (i >= j) break;

            exch(a, i, j);
            if (eq(a[i], v, comparator)) exch(a, ++p, i);
            if (eq(a[j], v, comparator)) exch(a, --q, j);
        }


        i = j + 1;
        for (int k = lo; k <= p; k++)
            exch(a, k, j--);
        for (int k = hi; k >= q; k--)
            exch(a, k, i++);

        return IntPair.simple(j, i);
    }

    public static int binarySearch(int[] a, int key) {
        return binarySearch(a, 0, a.length - 1, key, Integer::compare);
    }

    public static int binarySearch(int[] a, int lo, int hi, int key, IntComparator comparator) {
        int low = lo;
        int high = hi;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = a[mid];

            if (less(midVal, key, comparator)) {
                low = mid + 1;
            } else if (more(midVal, key, comparator))
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.

    }

    public static int binarySearch(long[] a, long key) {
        return binarySearch(a, 0, a.length - 1, key, Long::compare);
    }

    public static int binarySearch(long[] a, int lo, int hi, long key, LongComparator comparator) {
        int low = lo;
        int high = hi;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            long midVal = a[mid];

            if (less(midVal, key, comparator)) {
                low = mid + 1;
            } else if (more(midVal, key, comparator))
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.

    }

    private static <T extends Comparable<T>> int getMedianIndex(T[] a, int lo, int hi) {
        int m;
        int n = hi - lo + 1;
        if (n <= MEDIAN_OF_3_CUTOFF) {
            m = median3(a, lo, lo + n / 2, hi);
        }
        // use Tukey ninther as partitioning element
        else {
            int eps = n / 8;
            int mid = lo + n / 2;
            int m1 = median3(a, lo, lo + eps, lo + eps + eps);
            int m2 = median3(a, mid - eps, mid, mid + eps);
            int m3 = median3(a, hi - eps - eps, hi - eps, hi);
            m = median3(a, m1, m2, m3);
        }
        return m;
    }

    private static int getMedianIndex(int[] a, int lo, int hi, IntComparator comparator) {
        int m;
        int n = hi - lo + 1;
        if (n <= MEDIAN_OF_3_CUTOFF) {
            m = median3(a, lo, lo + n / 2, hi, comparator);
        }
        // use Tukey ninther as partitioning element
        else {
            int eps = n / 8;
            int mid = lo + n / 2;
            int m1 = median3(a, lo, lo + eps, lo + eps + eps, comparator);
            int m2 = median3(a, mid - eps, mid, mid + eps, comparator);
            int m3 = median3(a, hi - eps - eps, hi - eps, hi, comparator);
            m = median3(a, m1, m2, m3, comparator);
        }
        return m;
    }

    private static int getMedianIndex(long[] a, int lo, int hi, LongComparator comparator) {
        int m;
        int n = hi - lo + 1;
        if (n <= MEDIAN_OF_3_CUTOFF) {
            m = median3(a, lo, lo + n / 2, hi, comparator);
        }
        // use Tukey ninther as partitioning element
        else {
            int eps = n / 8;
            int mid = lo + n / 2;
            int m1 = median3(a, lo, lo + eps, lo + eps + eps, comparator);
            int m2 = median3(a, mid - eps, mid, mid + eps, comparator);
            int m3 = median3(a, hi - eps - eps, hi - eps, hi, comparator);
            m = median3(a, m1, m2, m3, comparator);
        }
        return m;
    }

    private static <T extends Comparable<T>> int median3(T[] a, int lo, int m, int hi) {
        return (less(a[lo], a[m]) ?
                (less(a[m], a[hi]) ? m : less(a[lo], a[hi]) ? hi : lo) :
                (less(a[hi], a[m]) ? m : less(a[hi], a[lo]) ? hi : lo));
    }

    private static int median3(int[] a, int lo, int m, int hi, IntComparator comparator) {
        return (less(a[lo], a[m], comparator) ?
                (less(a[m], a[hi], comparator) ? m : less(a[lo], a[hi], comparator) ? hi : lo) :
                (less(a[hi], a[m], comparator) ? m : less(a[hi], a[lo], comparator) ? hi : lo));
    }

    private static int median3(long[] a, int lo, int m, int hi, LongComparator comparator) {
        return (less(a[lo], a[m], comparator) ?
                (less(a[m], a[hi], comparator) ? m : less(a[lo], a[hi], comparator) ? hi : lo) :
                (less(a[hi], a[m], comparator) ? m : less(a[hi], a[lo], comparator) ? hi : lo));
    }

    private static <T extends Comparable<T>> boolean eq(T a, T b) {
        return compare(a, b) == 0;
    }

    private static boolean eq(int a, int b, IntComparator comparator) {
        return comparator.compare(a, b) == 0;
    }

    private static boolean eq(long a, long b, LongComparator comparator) {
        return comparator.compare(a, b) == 0;
    }

    private static <T extends Comparable<T>> int compare(T a, T b) {
        return a.compareTo(b);
    }

    private static int compare(int a, int b, IntComparator comparator) {
        return comparator.compare(a, b);
    }

    private static int compare(long a, long b, LongComparator comparator) {
        return comparator.compare(a, b);
    }

    public static void main(String[] args) {
//        Integer[] a1 = {1, 2, 4, 6, 1, 2, 3, 5, 7, 8, 9};
//        Integer[] a1 = {1, 2, 4, 6, 1, 2, 3, 5};
        int[] a2 = {1, 2, 3, 5, 7, 8, 9};
        Integer value = null;
        int index = binarySearch(a2, 1);
        if (index >= 0) {
            value = a2[index];
        } else if (index < -1) {
            index = -(index + 2);
            value = a2[index];
        }
        System.out.println(value);
////        merge2(a1, 0, 3, a1.length - 1);
////        System.out.println(Arrays.toString(a1));
////        int n = 1000000;
//        int trails = 100_000;
////        int trails = 100;
//        for (int n = 2; n < 1_0_000_000; n *= 2) {
//            int lo = 0;
//            int mid = lo + n / 2;
//            int hi = n - 1;
//
////            System.out.printf("%6d: merge->%s\tmerge2->%s%n",
////                    n,
////                    timeRandomInput(a -> merge(a, new Double[a.length], lo, mid, hi), n, trails),
////                    timeRandomInput(a -> merge(a, lo, mid, hi), n, trails));
//        }
    }

    public static void swim(int[] heap, int index) {
        while (index > 1 && less(heap[index / 2], heap[index])) {
            exch(heap, index / 2, index);
            index = index / 2;
        }
    }

    public static void sink(int[] heap, int index, int heapSize) {
        while (index * 2 <= heapSize) {
            int j = index * 2;
            if (j < heapSize && less(heap[j], heap[j + 1])) j++;
            if (!less(heap[index], heap[j])) break;
            exch(heap, index, j);
            index = j;
        }
    }

    public static <T> void swim(T[] heap, int index, Comparator<T> comparator) {
        while (index > 1 && less(heap[index / 2], heap[index], comparator)) {
            exch(heap, index / 2, index);
            index = index / 2;
        }
    }

    public static <T> void swim0(T[] heap, int index, Comparator<T> comparator) {
        int parent = max(index / 2 - 1, 0);
        while (index >= 1 && less(heap[parent], heap[index], comparator)) {
            exch(heap, parent, index);
            index = parent;
        }
    }

    public static <T> void sink(T[] heap, int index, int heapSize, Comparator<T> comparator) {
        while (index * 2 <= heapSize) {
            int j = index * 2;
            //check end of heap and choose biggest node
            if (j < heapSize && less(heap[j], heap[j + 1], comparator)) j++;
            if (!less(heap[index], heap[j], comparator)) break;
            exch(heap, index, j);
            index = j;
        }
    }

    public static <T> void sink0(T[] heap, int index, int heapSize, Comparator<T> comparator) {
        while (index * 2 <= heapSize) {
            int j = index * 2;
            //check end of heap and choose biggest node
            if (j < heapSize && less(heap[j - 1], heap[j], comparator)) j++;
            if (!less(heap[index - 1], heap[j - 1], comparator)) break;
            exch(heap, index - 1, j - 1);
            index = j;
        }
    }

    public static void move(Object[] a, int from, int to) {
        a[to] = a[from];
        a[from] = null;
    }

    public static <T> boolean eq(T a, T b, Comparator<T> cmp) {
        return cmp.compare(a, b) == 0;
    }

}
