package net.tifoha.utils.algorithms.search.graph;

import java.util.Collection;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {
        CharMatrix matrix = CharMatrix
                .builder()
                .fillWithCharDigits("" +
                        "1 1 1 1 1 1 8 1 1 1" +
                        "1 1 # # # 1 1 X 1 1" +
                        "1 1 1 1 1 # # 1 1 1" +
                        "1 1 1 1 1 1 # 1 1 1" +
                        "A 1 1 1 1 1 # 1 1 1" +
                        "1 1 1 1 1 1 # 1 1 1" +
                        "1 1 1 1 1 1 # 1 1 1" +
                        "1 # # # # # # 1 1 1" +
                        "1 1 1 1 1 1 1 1 1 1" +
                        "1 1 1 1 1 1 1 1 1 1")
                .build();

        System.out.println(matrix);
        System.out.println();

        GraphEntry<Character> start = matrix.getStart();
        GraphEntry<Character> finish = matrix.getFinish();

        Stream.<PathFinder<Character>>of(
                new BreathFirstPathFinder<>(),
                new DijkstraPathFinder<>(),
                new DijkstraHeuristicPathFinder<>(matrix::distance),
                new AStarPathFinder<>(matrix::distance)
        )
                .peek(pf -> System.out.print(pf.getClass().getSimpleName() + ":"))
                .map(pf -> pf.findPath(start, finish))
                .peek(pf -> System.out.println("cost: " + calculateCost(pf)))
                .peek(pf -> System.out.println(matrix.withPath(pf)))
                .forEach(System.out::println);
    }

    private static double calculateCost(Collection<GraphEntry<Character>> path) {
        return path.stream().mapToInt(GraphEntry::getCost).sum();
    }
}
