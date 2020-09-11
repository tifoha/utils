package net.tifoha.utils.algorithms.search.graph;

public interface GraphEntry<T> {
    Iterable<GraphEntry<T>> getNeighbors();

    int getCost();
}
