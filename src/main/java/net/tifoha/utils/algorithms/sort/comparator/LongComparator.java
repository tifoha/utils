package net.tifoha.utils.algorithms.sort.comparator;

/**
 * @author Vitalii Sereda
 */
@FunctionalInterface
public interface LongComparator {
    LongComparator NATURAL_ORDER_COMPARATOR = Long::compare;

    static LongComparator naturalOrder() {
        return NATURAL_ORDER_COMPARATOR;
    }

    int compare(long a, long b);
}
