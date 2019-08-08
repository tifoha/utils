package net.tifoha.utils.binary;

/**
 * @author Vitalii Sereda
 */
public class BinaryUtils {
    private BinaryUtils() {
    }

    public static long toLong(int a, int b) {
        return (((long) a) << 32) | (b & 0xffffffffL);
    }

    public static int[] toInts(long l) {
        return new int[]{getHeadInt(l), getTailInt(l)};
    }

    public static int getTailInt(long l) {
        return (int) (l & 0xffffffffL);
    }

    public static int getHeadInt(long l) {
        return (int) (l >>> 32);
    }

}
