package net.tifoha.utils.algorithms.search.graph;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import static java.lang.Math.*;

public abstract class Matrix<T> {
    protected final T[] data;
    private final int size;
    private final ToIntFunction<T> costDetector;
    private final Predicate<T> wallTester;

    protected Matrix(T[] data, ToIntFunction<T> costDetector, Predicate<T> wallTester) {
        this.data = data;
        this.size = (int) Math.sqrt(data.length);
        this.costDetector = costDetector;
        this.wallTester = wallTester;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(String.format("%2s", data[0]));
        for (int i = 1; i < data.length; i++) {
            if (i % size == 0) {
                sb.append('\n');
            } else {
                sb.append(' ');
            }
            sb.append(String.format("%2s", data[i]));
        }
        return sb.toString();
    }

    public T get(int x, int y) {
        return data[y * size + x];
    }

    public void set(int x, int y, T value) {
        this.data[y * size + x] = value;
    }

    public GraphEntry<T> getGraphEntry(int x, int y) {
        return new MatrixGraphEntry(x, y);
    }

    public GraphEntry<T> getGraphEntry(int index) {
        return new MatrixGraphEntry(index);
    }

    public double distance(GraphEntry<T> fromEntry, GraphEntry<T> toEntry) {
        MatrixGraphEntry from = (MatrixGraphEntry) fromEntry;
        MatrixGraphEntry to = (MatrixGraphEntry) toEntry;
        return sqrt(pow(from.x - to.x, 2) + pow(from.y - to.y, 2));

    }

    public String withPath(Collection<GraphEntry<T>> path) {
        Iterator<Integer> iter = path
                .stream()
                .map(e -> (MatrixGraphEntry) e)
                .map(MatrixGraphEntry::getIndex)
                .sorted()
                .iterator();

        int current = iter.next();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            String str = String.format("%2s", data[i]);
            if (i == current) {
                str = String.format("%2s", 'O');
                current = iter.hasNext() ? iter.next() : current;
            }
            if (i != 0 && i % size == 0) {
                sb.append('\n');
            } else {
                sb.append(' ');
            }
            sb.append(str);
        }
        return sb.deleteCharAt(0).toString();
    }

    protected class MatrixGraphEntry implements GraphEntry<T> {

        private final int index;
        private int x;
        private int y;

        protected MatrixGraphEntry(int index) {
            this.index = index;
            this.x = index % size;
            this.y = index / size;
        }

        private MatrixGraphEntry(int x, int y) {
            this.index = y * size + x;
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Iterable<GraphEntry<T>> getNeighbors() {
            List<GraphEntry<T>> neighbors = new LinkedList<>();
            MatrixGraphEntry neighbor;

            neighbor = up();
            if (canUse(neighbor)) {
                neighbors.add(neighbor);
            }

            neighbor = right();
            if (canUse(neighbor)) {
                neighbors.add(neighbor);
            }

            neighbor = down();
            if (canUse(neighbor)) {
                neighbors.add(neighbor);
            }

            neighbor = left();
            if (canUse(neighbor)) {
                neighbors.add(neighbor);
            }

            return neighbors;
        }

        @Override
        public int getCost() {
            return costDetector.applyAsInt(data[index]);
        }

        @Override
        public String toString() {
            return "" + getX() + ":" + getY() + ":" + get();
        }

        private T get() {
            return data[index];
        }

        public void set(T value) {
            Matrix.this.data[index] = value;
        }

        public MatrixGraphEntry up() {
            MatrixGraphEntry result = null;

            if (y > 0) {
                result = new MatrixGraphEntry(x, y - 1);
            }

            return result;
        }

        public MatrixGraphEntry down() {
            MatrixGraphEntry result = null;

            if (y < size - 1) {
                result = new MatrixGraphEntry(x, y + 1);
            }


            return result;
        }

        public MatrixGraphEntry left() {
            MatrixGraphEntry result = null;

            if (x > 0) {
                result = new MatrixGraphEntry(x - 1, y);
            }

            return result;
        }

        public MatrixGraphEntry right() {
            MatrixGraphEntry result = null;

            if (x < size - 1) {
                result = new MatrixGraphEntry(x + 1, y);
            }

            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MatrixGraphEntry that = (MatrixGraphEntry) o;
            return index == that.index;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index);
        }

        public int getIndex() {
            return index;
        }
    }

    private boolean canUse(MatrixGraphEntry neighbor) {
        return neighbor != null && !wallTester.test(neighbor.get());
    }


}
