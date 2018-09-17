package net.tifoha.utils.algorithms.search.graph;

import java.util.*;

/**
 * @author Vitalii Sereda
 */
public class DijkstraPathFinder<T> extends BreathFirstPathFinder<T> {
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

        PathFinder<Character> pathFinder = new DijkstraPathFinder<>();
        System.out.println(matrix);
        System.out.println();

        GraphEntry<Character> start = matrix.getGraphEntry(0, 0);
        GraphEntry<Character> end = matrix.getGraphEntry(3, 3);
        Collection<GraphEntry<Character>> path = pathFinder.findPath(start, end);

        System.out.println(path);
    }

    @Override
    public Collection<GraphEntry<T>> findPath(GraphEntry<T> start, GraphEntry<T> end) {
        Queue<BreadCrumbWithCost<T>> queue = new PriorityQueue<>();
        Map<GraphEntry<T>, Double> processed = new HashMap<>();

        processed.put(start, 0.0);
        queue.offer(new BreadCrumbWithCost<>(start, null, 0));

        int steps = 0;
        while (!queue.isEmpty()) {
            BreadCrumbWithCost<T> currentBreadCrumb = queue.remove();
            GraphEntry<T> currentEntry = currentBreadCrumb.getEntry();

            for (GraphEntry<T> entry : currentEntry.getNeighbors()) {
                double cost = currentBreadCrumb.getCost() + entry.getCost();
                if (!processed.containsKey(entry) || processed.get(entry) > cost) {
                    BreadCrumbWithCost<T> breadCrumb = new BreadCrumbWithCost<>(entry, currentBreadCrumb, cost);
                    if (Objects.equals(end, entry)) {
                        System.out.println(steps);
                        return decodePath(breadCrumb);
                    }
                    processed.put(entry, cost);
                    queue.offer(breadCrumb);
                }
            }
            steps++;
        }

        System.out.println(steps);
        return Collections.emptySet();
    }

    protected static class BreadCrumbWithCost<T> extends BreathFirstPathFinder.BreadCrumb<T> implements Comparable<BreadCrumbWithCost<T>> {
        protected final double cost;

        public BreadCrumbWithCost(GraphEntry<T> entry, BreadCrumbWithCost<T> cameFrom, double cost) {
            super(entry, cameFrom);
            this.cost = cost;
        }

        @Override
        public int compareTo(BreadCrumbWithCost<T> that) {
            if (this.cost > that.cost) return 1;
            else if (this.cost < that.cost) return -1;
            else return 0;
        }

        @Override
        public String toString() {
            return entry.toString() + "=" + cost;
        }

        public GraphEntry<T> getEntry() {
            return entry;
        }

        public double getCost() {
            return cost;
        }

        public BreadCrumb<T> getCameFrom() {
            return cameFrom;
        }
    }
}
