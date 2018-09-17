package net.tifoha.utils.algorithms.search.graph;

import java.util.Collection;

/**
 * @author Vitalii Sereda
 */
public interface PathFinder<T> {
     Collection<GraphEntry<T>> findPath(GraphEntry<T> start, GraphEntry<T> end);
}
