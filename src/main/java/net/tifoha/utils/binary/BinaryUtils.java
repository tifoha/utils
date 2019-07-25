package net.tifoha.utils.binary;

/**
 * @author Vitalii Sereda
 */
public class BinaryUtils {
    private BinaryUtils() { }

    public static long toLong(int a, int b) {
        return (((long) a) << 32) | (b & 0xffffffffL);
    }

    public static int[] toInts(long l) {
        return new int[]{(int) (l >> 32), (int) l};
    }
}
