package net.tifoha.utils.algorithms.misc;

/**
 * @author Vitalii Sereda
 */
public interface IntPair {
    int getFirst();

    int getSecond();

    static IntPair simple(int first, int second) {
        return new SimpleIntPair(first, second);
    }

    static IntPair compact(int first, int second) {
        return new CompactIntPair(first, second);
    }

    static IntPair compact(long value) {
        return new CompactIntPair(value);
    }
}
