package net.tifoha.utils.algorithms.structures.trees;

public class SingleIntInterval implements IntInterval {
    private final int value;

    public SingleIntInterval(int value) {
        this.value = value;
    }

    @Override
    public int getLeft() {
        return value;
    }

    @Override
    public int getRight() {
        return value;
    }

    @Override
    public boolean contains(int value) {
        return value == this.value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
