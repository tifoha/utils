package net.tifoha.utils.algorithms.search.graph;

import java.util.*;

public class BreathFirstPathFinder<T> implements PathFinder<T>{
    public static void main(String[] args) {
        CharMatrix matrix = CharMatrix
                .builder()
                .fillWithCharDigits(
                                "1 1 1 1 1 1" +
                                "1 8 0 0 0 1" +
                                "1 8 8 0 0 1" +
                                "1 0 8 9 0 1" +
                                "1 0 1 0 1 1" +
                                "0 0 1 1 1 0")
                .build();

        PathFinder<Character> pathFinder = new BreathFirstPathFinder<>();
        System.out.println(matrix);
        System.out.println();

        GraphEntry<Character> start = matrix.getGraphEntry(0, 0);
        GraphEntry<Character> end = matrix.getGraphEntry(3, 3);
        Collection<GraphEntry<Character>> path = pathFinder.findPath(start, end);

        System.out.println(path);
    }

    @Override
    public Collection<GraphEntry<T>> findPath(GraphEntry<T> start, GraphEntry<T> end) {
        Queue<BreadCrumb<T>> queue = new LinkedList<>();
        Set<GraphEntry<T>> processed = new HashSet<>();

        processed.add(start);
        queue.offer(new BreadCrumb<>(start));

        int steps=0;
        while (!queue.isEmpty()) {
            BreadCrumb<T> currentBreadCrumb = queue.remove();
            GraphEntry<T> currentEntry = currentBreadCrumb.getEntry();

            for (GraphEntry<T> entry : currentEntry.getNeighbors()) {
                if (!processed.contains(entry)) {
                    BreadCrumb<T> breadCrumb = new BreadCrumb<>(entry, currentBreadCrumb);
                    if (Objects.equals(end, entry)) {
                        System.out.println(steps);
                        return decodePath(breadCrumb);
                    }
                    processed.add(entry);
                    queue.offer(breadCrumb);
                }
            }
            steps++;
        }

        System.out.println(steps);
        return Collections.emptySet();
    }

    protected Collection<GraphEntry<T>> decodePath(BreadCrumb<T> end) {
        Deque<GraphEntry<T>> result = new LinkedList<>();

        BreadCrumb<T> breadCrumb = end;
        while (breadCrumb != null) {
            result.offerFirst(breadCrumb.getEntry());
            breadCrumb = breadCrumb.getCameFrom();
        }

        return result;
    }

    protected static class BreadCrumb<T> {
        protected final GraphEntry<T> entry;
        protected final BreadCrumb<T> cameFrom;

        private BreadCrumb(GraphEntry<T> entry) {
            this(entry, null);
        }

        public BreadCrumb(GraphEntry<T> entry, BreadCrumb<T> cameFrom) {
            this.entry = entry;
            this.cameFrom = cameFrom;
        }

        public GraphEntry<T> getEntry() {
            return entry;
        }

        public BreadCrumb<T> getCameFrom() {
            return cameFrom;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BreadCrumb<?> that = (BreadCrumb<?>) o;
            return Objects.equals(entry, that.entry);
        }

        @Override
        public int hashCode() {

            return Objects.hash(entry);
        }

        @Override
        public String toString() {
            return entry.toString();
        }
    }


//    public static void main(String[] args) {
//        IntMatrix matrix = new IntMatrix(new int[]{
//                1, 1, 1, 1, 1, 1,
//                1, 1, 0, 0, 0, 1,
//                0, 1, 1, 0, 0, 1,
//                0, 0, 1, 9, 0, 1,
//                0, 0, 0, 1, 1, 1,
//                0, 0, 0, 0, 0, 0,
//        });
//        System.out.println(matrix);
//        System.out.println();
//        Queue<IntMatrix.IntMatrixEntry> queue = new LinkedList<>();
//        Set<IntMatrix.IntMatrixEntry> visited = new HashSet<>();
//        IntMatrix.IntMatrixEntry start = matrix.getEntry(0, 0);
//        queue.offer(start);
//        visited.add(start);
//        int i = 0;
//        while (!queue.isEmpty()) {
//            IntMatrix.IntMatrixEntry current = queue.remove();
////            current.set(++i);
//            for (IntMatrix.IntMatrixEntry entry : current.getNeighbors()) {
//                i++;
//                if (entry.get() > 0 && !visited.contains(entry)) {
//                    entry.setCameFrom(current);
//                    if (entry.get() == 9) {
//                        System.out.println(entry.pathString());
//                        break;
//                    }
//                    queue.offer(entry);
//                    visited.add(entry);
//                }
//            }
//        }
//
//        System.out.println(matrix);
//        System.out.println(i);
//    }
//
//    private static class IntMatrix {
//
//        protected final int[] data;
//        private final int size;
//
//        @SuppressWarnings("unchecked")
//        private IntMatrix(int size) {
//            this.size = size;
//            this.data = new int[size * size];
//        }
//
//        public IntMatrix(int[] data) {
//            this.data = data;
//            size = (int) Math.sqrt(data.length);
//        }
//
//        @Override
//        public String toString() {
//            final StringBuilder sb = new StringBuilder(String.format("%2d", data[0]));
//            for (int i = 1; i < data.length; i++) {
//                if (i % size == 0) {
//                    sb.append('\n');
//                } else {
//                    sb.append(' ');
//                }
//                sb.append(String.format("%2d", data[i]));
//            }
//            return sb.toString();
//        }
//
//        public IntMatrixEntry getEntry(int x, int y) {
//            return new IntMatrixEntry(y * size + x);
//        }
//
//        public int get(int x, int y) {
//            return data[y * size + x];
//        }
//
//        public void set(int x, int y, int data) {
//            this.data[y * size + x] = data;
//        }
//
//        private class IntMatrixEntry {
//            private final int index;
//            private int x;
//            private int y;
//            private IntMatrixEntry cameFrom;
//
//            private IntMatrixEntry(int index) {
//                this.index = index;
//                this.x = index % size;
//                this.y = index / size;
//            }
//
//            private IntMatrixEntry(int x, int y) {
//                this.index = y * size + x;
//                this.x = x;
//                this.y = y;
//            }
//
//            public int getX() {
//                return x;
//            }
//
//            public int getY() {
//                return y;
//            }
//
//            public Collection<IntMatrixEntry> getNeighbors() {
//                List<IntMatrixEntry> neighbors = new LinkedList<>();
//                IntMatrixEntry neighbor;
//
//                neighbor = up();
//                if (neighbor != null) {
//                    neighbors.add(neighbor);
//                }
//
//                neighbor = right();
//                if (neighbor != null) {
//                    neighbors.add(neighbor);
//                }
//
//                neighbor = down();
//                if (neighbor != null) {
//                    neighbors.add(neighbor);
//                }
//
//                neighbor = left();
//                if (neighbor != null) {
//                    neighbors.add(neighbor);
//                }
//
//                return neighbors;
//            }
//
//            @Override
//            public String toString() {
//                return "" + getX() + ":" + getY() + ":" + get();
//            }
//
//            private String pathString() {
//                Deque<IntMatrix.IntMatrixEntry> path = new LinkedList<>();
//                IntMatrix.IntMatrixEntry entry = this;
//
//                while (entry != null) {
//                    path.offerFirst(entry);
//                    entry = entry.getCameFrom();
//                }
//
//                return path
//                        .stream()
//                        .map(Objects::toString)
//                        .collect(Collectors.joining("->"));
//            }
//
//
//            private int get() {
//                return data[index];
//            }
//
//            public void set(int data) {
//                IntMatrix.this.data[index] = data;
//            }
//
//            public IntMatrixEntry up() {
//                IntMatrixEntry result = null;
//
//                if (y > 0) {
//                    result = new IntMatrixEntry(x, y - 1);
//                }
//
//                return result;
//            }
//
//            public IntMatrixEntry down() {
//                IntMatrixEntry result = null;
//
//                if (y < size - 1) {
//                    result = new IntMatrixEntry(x, y + 1);
//                }
//
//
//                return result;
//            }
//
//            public IntMatrixEntry left() {
//                IntMatrixEntry result = null;
//
//                if (x > 0) {
//                    result = new IntMatrixEntry(x - 1, y);
//                }
//
//                return result;
//            }
//
//            public IntMatrixEntry right() {
//                IntMatrixEntry result = null;
//
//                if (x < size - 1) {
//                    result = new IntMatrixEntry(x + 1, y);
//                }
//
//                return result;
//            }
//
//            @Override
//            public boolean equals(Object o) {
//                if (this == o) return true;
//                if (o == null || getClass() != o.getClass()) return false;
//                IntMatrixEntry that = (IntMatrixEntry) o;
//                return index == that.index;
//            }
//
//            @Override
//            public int hashCode() {
//
//                return Objects.hash(index);
//            }
//
//            public void setCameFrom(IntMatrixEntry cameFrom) {
//                this.cameFrom = cameFrom;
//            }
//
//            public IntMatrixEntry getCameFrom() {
//                return cameFrom;
//            }
//        }
//
//    }

}
