package net.tifoha.utils.algorithms.sort.comparator;

/**
 * @author Vitalii Sereda
 */
@FunctionalInterface
public interface IntComparator {
    IntComparator NATURAL_ORDER_COMPARATOR = Integer::compare;

    static IntComparator naturalOrder() {
        return NATURAL_ORDER_COMPARATOR;
    }

    int compare(int a, int b);
}
