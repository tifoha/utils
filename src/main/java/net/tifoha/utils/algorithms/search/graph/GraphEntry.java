package net.tifoha.utils.algorithms.search.graph;

/**
 * @author Vitalii Sereda
 */
public interface GraphEntry<T> {
    Iterable<GraphEntry<T>> getNeighbors();

    int getCost();
}
