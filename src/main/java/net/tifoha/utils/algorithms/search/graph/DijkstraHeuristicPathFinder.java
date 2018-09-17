package net.tifoha.utils.algorithms.search.graph;

import java.util.*;
import java.util.function.ToDoubleBiFunction;

/**
 * @author Vitalii Sereda
 */
public class DijkstraHeuristicPathFinder<T> extends DijkstraPathFinder<T> {
    protected final ToDoubleBiFunction<GraphEntry<T>, GraphEntry<T>> heuristicFunction;

    public DijkstraHeuristicPathFinder(ToDoubleBiFunction<GraphEntry<T>, GraphEntry<T>> heuristicFunction) {
        this.heuristicFunction = heuristicFunction;
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
                double priority = heuristicFunction.applyAsDouble(entry, end);
                if (!processed.containsKey(entry) || processed.get(entry) > priority) {
                    BreadCrumbWithCost<T> breadCrumb = new BreadCrumbWithCost<>(entry, currentBreadCrumb, priority);
                    if (Objects.equals(end, entry)) {
                        System.out.println(steps);
                        return decodePath(breadCrumb);
                    }
                    processed.put(entry, priority);
                    queue.offer(breadCrumb);
                }
            }
            steps++;
        }

        System.out.println(steps);
        return Collections.emptySet();
    }

    public static void main(String[] args) {
        CharMatrix matrix = CharMatrix
                .builder()
                .fillWithCharDigits("" +
                        "1 1 1 1 1 1 1 1 1 1" +
                        "# 1 # # # # # X 1 1" +
                        "1 1 1 1 1 1 # 1 1 1" +
                        "1 1 1 1 1 1 # 1 1 1" +
                        "A 1 1 1 1 1 # 1 1 1" +
                        "1 1 1 1 1 1 # 1 1 1" +
                        "1 1 1 1 1 1 # 1 1 1" +
                        "1 # # # # # # 1 1 1" +
                        "1 1 1 1 1 1 1 1 1 1" +
                        "1 1 1 1 1 1 1 1 1 1")
                .build();

        PathFinder<Character> pathFinder = new DijkstraHeuristicPathFinder<>(matrix::distance);
//        PathFinder<Character> pathFinder = new DijkstraPathFinder<>();
//        PathFinder<Character> pathFinder = new BreathFirstPathFinder<>();
        System.out.println(matrix);
        System.out.println();

        GraphEntry<Character> start = matrix.getStart();
        GraphEntry<Character> end = matrix.getFinish();
        Collection<GraphEntry<Character>> path = pathFinder.findPath(start, end);

        System.out.println(path);
        System.out.println(matrix.withPath(path));
    }
}
