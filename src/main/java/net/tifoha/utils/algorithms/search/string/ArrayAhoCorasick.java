package net.tifoha.utils.algorithms.search.string;

/**
 * @author Vitalii Sereda
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// https://en.wikipedia.org/wiki/Aho–Corasick_algorithm
public class ArrayAhoCorasick implements StringSearcher {
    //    private static final char CHAR = 'a';
    //    static final int ALPHABET_SIZE = 26;
    static final int ALPHABET_SIZE = 94;
    private static final char CHAR = ' ';
    Node[] nodes;
    int nodeCount;

    @Override
    public long countTotal(String str) {
        int count = 0;
        int node = 0;
        for (int i = 0; i < str.length(); i++) {
            node = transition(node, str.charAt(i));
            if (nodes[node].leaf)
                count++;
        }
        return count;
    }

    public static class Node {
        int parent;
        char charFromParent;
        int suffLink = -1;
        int[] children = new int[ALPHABET_SIZE];
        int[] transitions = new int[ALPHABET_SIZE];
        boolean leaf;

        public Node() {
            Arrays.fill(children, -1);
        }
    }

    public ArrayAhoCorasick(int maxNodes) {
        nodes = new Node[maxNodes];
        // create root
        nodes[0] = new Node();
        nodes[0].suffLink = 0;
        nodes[0].parent = -1;
        nodeCount = 1;
    }

    @Override
    public void addString(String s) {
        int cur = 0;
        for (char ch : s.toCharArray()) {
            int c = ch - CHAR;
            if (nodes[cur].children[c] == -1) {
                nodes[nodeCount] = new Node();
                nodes[nodeCount].parent = cur;
                nodes[nodeCount].charFromParent = ch;
                nodes[cur].children[c] = nodeCount++;
            }
            cur = nodes[cur].children[c];
        }
        nodes[cur].leaf = true;
    }

    public int suffLink(int nodeIndex) {
        Node node = nodes[nodeIndex];
        if (node.suffLink == -1) {

            if (node.parent == 0) {
                node.suffLink = 0;
            } else {
                int parentSuffLink = suffLink(node.parent);
                node.suffLink = transition(parentSuffLink, node.charFromParent);
            }
        }
        return node.suffLink;
    }

    public int transition(int nodeIndex, char ch) {
        int c = ch - CHAR;
        Node node = nodes[nodeIndex];
        if (node.children[c] != -1) {
            return node.children[c];
        } else {
            if (nodeIndex == 0) {
                return 0;
            } else {
                return transition(suffLink(nodeIndex), ch);
            }
        }
    }

    // Usage example
    public static void main(String[] args) {
        for (int i = 0; i < 95; i++) {
            char c = (char) (' ' + i);
            System.out.printf("%s\t%s\t%s%n", (int) c, c, i);
        }

        ArrayAhoCorasick ahoCorasick = new ArrayAhoCorasick(1000);
        ahoCorasick.addString("breakfast");
        ahoCorasick.addString("beach");
        ahoCorasick.addString("citycenter");
        ahoCorasick.addString("city");
        ahoCorasick.addString("location");
        ahoCorasick.addString("metro");
        ahoCorasick.addString("view");
        ahoCorasick.addString("staff");
        ahoCorasick.addString("pric");
        List<String> strings = new ArrayList<>();
        strings.add("This hotel has a nice view of the citycenter. The location is perfect.");
        strings.add("The breakfast is ok. Regarding location, it is quite far from citycenter but price is cheap so it is worth.");
        strings.add("Location is excellent, 5 minutes from citycenter. There is also a metro station very close to the hotel.");
        strings.add("They said I couldn't take my dog and there were other guests with dogs! That is not fair.");
        strings.add("Very friendly staff and good cost-benefit ratio. Its location is a bit far from citycenter.");

        for (String s : strings) {
            int node = 0;
            List<Integer> positions = new ArrayList<>();
            for (int i = 0; i < s.length(); i++) {
                node = ahoCorasick.transition(node, s.charAt(i));
                if (ahoCorasick.nodes[node].leaf)
                    positions.add(i);
            }
            System.out.println(positions);
        }
    }
}