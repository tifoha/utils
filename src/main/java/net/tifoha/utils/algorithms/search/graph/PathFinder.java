package net.tifoha.utils.algorithms.search.graph;

import java.util.Collection;

public interface PathFinder<T> {
     Collection<GraphEntry<T>> findPath(GraphEntry<T> start, GraphEntry<T> end);
}
