package net.tifoha.utils.algorithms.misc;

import net.tifoha.utils.binary.BinaryUtils;

/**
 * @author Vitalii Sereda
 */
public class CompactIntPair implements IntPair {
    private final long value;

    public CompactIntPair(int first, int second) {
        value = BinaryUtils.toLong(first, second);
    }

    public CompactIntPair(long value) {
        this.value = value;
    }

    @Override
    public int getFirst() {
        return (int) (value >> 32);
    }

    @Override
    public int getSecond() {
        return (int) value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('[')
                .append(getFirst())
                .append("..")
                .append(getSecond())
                .append(']');
        return sb.toString();
    }
}
