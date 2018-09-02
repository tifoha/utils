package net.tifoha.utils.algorithms.search.string;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vitalii Sereda
 */
public class ObjectAhoCorasick implements StringSearcher{
    private final Node root;

    public ObjectAhoCorasick() {
        root = new Node('\0');
        root.parent = root;
        root.suffixLink = root;
    }

    public void addString(String str) {
        Node node = root;
        for (char c : str.toCharArray()) {
            node = node.getOrNewChild(c);
        }
        node.setWord(str);
    }

    @Override
    public long countTotal(String str) {
        int count = 0;
        Node node = root;
        for (char c : str.toCharArray()) {
            node = node.find(c);
            if (node.leaf) {
                count++;
            }
        }

        return count;
    }

    public Map<String, List<Long>> find(String str) {
        Map<String, List<Long>> result = new HashMap<>();
        Node node = root;
        long currentChar = 0;
        for (char c : str.toCharArray()) {
            node = node.find(c);
            if (node.leaf) {
                result
                        .computeIfAbsent(node.word, w -> new ArrayList<>())
                        .add(currentChar - node.word.length() + 1);
            }
            currentChar++;
        }
        return result;
    }

    private class Node {
        private final char symbol;
        private final Map<Character, Node> children = new HashMap<>();
        private Node suffixLink;
        private Node parent;
        private boolean leaf;
        private String word;

        private Node(Node parent, char symbol) {
            this.parent = parent;
            this.symbol = symbol;
        }

        public Node(char symbol) {
            this(null, symbol);
        }

        public Node getChild(char c) {
            return children.get(c);
        }

        public void setWord(String str) {
            leaf = true;
            word = str;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder()
                    .append(symbol);

            if (leaf) {
                sb
                        .insert(0, '{')
                        .append('}');
            } else {
                sb
                        .insert(0, '[')
                        .append(']');
            }

            return sb.toString();
        }

        public Node getOrNewChild(char symbol) {
            return children.computeIfAbsent(symbol, c -> new Node(this, symbol));
        }

        public Node find(char symbol) {
            Node child = children.get(symbol);
            if (child == null) {
                if (this == root) {
                    return this;
                }
                return getSuffixLink()
                        .find(symbol);
            }
            return child;
        }

        private Node getSuffixLink() {
            if (suffixLink == null) {
                if (parent == root) {
                    suffixLink = parent;
                    return suffixLink;
                } else {
                    Node sf = parent
                            .getSuffixLink()
                            .find(this.symbol);
//                    if (sf != root) {
                        suffixLink = sf;
//                    }
                    return sf;
                }

            }
            return suffixLink;

        }
    }

    public static void main(String[] args) {
        ObjectAhoCorasick karasik = new ObjectAhoCorasick();
        karasik.addString("breakfast");
        karasik.addString("beach");
        karasik.addString("citycenter");
        karasik.addString("city");
        karasik.addString("location");
        karasik.addString("metro");
        karasik.addString("view");
        karasik.addString("staff");
        karasik.addString("pric");
//        karasik.addString("bd");
//        karasik.addString("abc");

        System.out.println(karasik);
        String str = "This hotel has a nice view of the citycenter. The location is perfect.";
        Map<String, List<Long>> result = karasik.find(str);
//        long result = karasik.count(str);
//        long result = karasik.count("abdabc");
        System.out.println(result);
    }
}
