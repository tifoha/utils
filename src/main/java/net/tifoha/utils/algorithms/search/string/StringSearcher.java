package net.tifoha.utils.algorithms.search.string;

import java.util.Map;

/**
 * @author Vitalii Sereda
 */
public interface StringSearcher {
    long countTotal(String str);

//    Map<String, Integer> findAll(String str);

    void addString(String s);
}
