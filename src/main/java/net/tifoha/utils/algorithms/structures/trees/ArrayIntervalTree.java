package net.tifoha.utils.algorithms.structures.trees;

import net.tifoha.utils.algorithms.sort.comparator.LongComparator;
import net.tifoha.utils.algorithms.sort.quick.Quick3WayMirror;
import net.tifoha.utils.binary.BinaryUtils;

import java.util.Collection;
import java.util.Random;
import java.util.stream.LongStream;

import static java.lang.Integer.compareUnsigned;

/**
 * @author Vitalii Sereda
 */
public class ArrayIntervalTree {
    private static final LongComparator COMPARATOR = (a, b) -> {
        int al = (int) (a >> 32);
        int ar = (int) a;
        int bl = (int) (b >> 32);
        int br = (int) b;
        if (compareUnsigned(al, br) > 0) {
            return 1;
        }

        if (compareUnsigned(bl, ar) > 0) {
            return -1;
        }

        if (compareUnsigned(al, br) == 0 && compareUnsigned(bl, ar) == 0) {
            return 0;
        }

        throw new IllegalArgumentException("Intersection: [" + al + "," + ar + "] x [" + bl + "," + br + "]");
    };
    private final long[] arr;

    public ArrayIntervalTree(long[] intervals) {
        arr = new long[intervals.length + 1];
        System.arraycopy(intervals, 0, arr, 1, intervals.length);
        Quick3WayMirror.sort(arr, 1, arr.length - 1, COMPARATOR);
    }

    public ArrayIntervalTree(Collection<IntInterval> intervals) {
        arr = new long[intervals.size() + 1];
        int i = 1;
        for (IntInterval interval : intervals) {
            arr[i++] = BinaryUtils.toLong(interval.getLeft(), interval.getRight());
        }
        Quick3WayMirror.sort(arr, 1, arr.length - 1, COMPARATOR);
    }

    public ArrayIntervalTree(LongStream intervals) {
        long[] tmp = intervals.toArray();
        Quick3WayMirror.sort(tmp, 0, tmp.length - 1, COMPARATOR);
        arr = new long[tmp.length + 1];
        System.arraycopy(tmp, 0, arr, 1, tmp.length);
    }

    public boolean isEmpty() {
        return arr.length == 0;
    }

    public static void main(String[] args) {
//        NormalIntInterval[] intervals = new NormalIntInterval[]{
//                of(1, 3),
//                of(7, 10),
//                of(11, 15),
//                of(30, 31),
//                of(16, 16),
//                of(18, 21),
//                of(22, 23),
//                of(4, 5),
//        };

//        ArrayIntervalTree tree = new ArrayIntervalTree(Arrays.asList(intervals));
        LongStream intervals = new Random().longs().limit(1_000_000);
        ArrayIntervalTree tree = new ArrayIntervalTree(intervals);
//        System.out.println(Arrays.stream(tree.arr).mapToObj(IntPair::compact).collect(toList()));
        System.out.println();
    }

}
