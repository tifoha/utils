package net.tifoha.utils.algorithms.misc;

/**
 * @author Vitalii Sereda
 */
public class SimpleIntPair implements IntPair {
    private final int first;
    private final int second;

    public SimpleIntPair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int getFirst() {
        return first;
    }

    @Override
    public int getSecond() {
        return second;
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
