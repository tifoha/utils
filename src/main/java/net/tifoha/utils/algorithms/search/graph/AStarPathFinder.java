package net.tifoha.utils.algorithms.search.graph;

import java.util.*;
import java.util.function.ToDoubleBiFunction;

public class AStarPathFinder<T> extends DijkstraHeuristicPathFinder<T> {

    public AStarPathFinder(ToDoubleBiFunction<GraphEntry<T>, GraphEntry<T>> heuristicFunction) {
        super(heuristicFunction);
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
                double newCost = processed.get(currentEntry) + entry.getCost();
                if (!processed.containsKey(entry) || processed.get(entry) > newCost) {
                    double priority = newCost + heuristicFunction.applyAsDouble(entry, end);
                    BreadCrumbWithCost<T> breadCrumb = new BreadCrumbWithCost<>(entry, currentBreadCrumb, priority);

                    if (Objects.equals(end, entry)) {
                        System.out.println(steps);
                        return decodePath(breadCrumb);
                    }

                    processed.put(entry, newCost);
                    queue.offer(breadCrumb);
                }
            }
            steps++;

        }
        System.out.println(steps);
        return Collections.emptySet();
    }
}
