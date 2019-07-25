package net.tifoha.utils.algorithms.structures.trees;

import java.util.Objects;

public class NormalIntInterval implements IntInterval {
    private final int left;
    private final int right;

    public NormalIntInterval(int left, int right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int getLeft() {
        return left;
    }

    @Override
    public int getRight() {
        return right;
    }

    @Override
    public boolean contains(int value) {
        return value >= left && value <= right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NormalIntInterval that = (NormalIntInterval) o;
        return left == that.left &&
                right == that.right;
    }

    @Override
    public int hashCode() {

        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        return left + ".." + right;
    }

    public static NormalIntInterval of(int left, int right) {
        return new NormalIntInterval(left, right);
    }
}
