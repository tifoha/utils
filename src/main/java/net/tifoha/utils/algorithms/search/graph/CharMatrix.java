package net.tifoha.utils.algorithms.search.graph;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;

/**
 * @author Vitalii Sereda
 */
public class CharMatrix extends Matrix<Character> {
    private CharMatrix(Character[] data, ToIntFunction<Character> costDetector, Predicate<Character> wallTester) {
        super(data, costDetector, wallTester);
    }

    public static CharMatrixBuilder builder() {
        return new CharMatrixBuilder();
    }

    public GraphEntry<Character> getStart() {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 'a' || data[i] == 'A') {
                return new MatrixGraphEntry(i);
            }
        }
        return null;
    }

    public GraphEntry<Character> getFinish() {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 'x' || data[i] == 'X') {
                return new MatrixGraphEntry(i);
            }
        }
        return null;

    }

    public static final class CharMatrixBuilder {
        private int size = 10;
        private ToIntFunction<Character> costDetector = CharMatrixBuilder::char2Cost;
        private Character[] data;
        private Predicate<Character> wallDetector = c -> c != 'a' && c != 'A' && c != 'x' && c != 'X' && !Character.isDigit(c);

        private CharMatrixBuilder() {
        }

        public CharMatrixBuilder size(int size) {
            this.size = size;
            return this;
        }

        public CharMatrixBuilder costDetector(ToIntFunction<Character> costDetector) {
            this.costDetector = costDetector;
            return this;
        }

        public CharMatrixBuilder fillWithChars(Character[] data, ToIntFunction<Character> costDetector) {
            this.costDetector = costDetector;
            this.data = data;
//            this.wallDetector = c -> !Character.isDigit((Character) c);
            return this;
        }

        public CharMatrixBuilder fillWithChars(Character[] data) {
            return fillWithChars(data, c -> CharMatrixBuilder.char2Cost(c));
        }


        public CharMatrixBuilder fillWithCharDigits(String matrix) {
            Character[] data = matrix
                    .chars()
                    .filter(codePoint -> !Character.isSpaceChar(codePoint))
                    .mapToObj(c -> (char) c)
                    .toArray(Character[]::new);
            return fillWithChars(data);
        }

        public CharMatrix build() {
            if (data != null) {
                return new CharMatrix(data, costDetector, wallDetector);
            }

            return new CharMatrix(new Character[size * size], costDetector, wallDetector);
        }

        private static int char2Cost(char c) {
            return Character.isDigit(c) ? Character.digit(c, 10) : 0;
        }
    }

}
