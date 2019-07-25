package net.tifoha.utils.algorithms.search.number;

/**
 * @author Vitalii Sereda
 */
public class IntArray {
    private final int[] ints;
    private final int size;

    public IntArray(int size) {
        this.size = size;
        ints = new int[this.size];
    }

    public int size() {
        return size;
    }

    public int getSetId(int i) {
        rangeCheck(i);
        return ints[i];
    }

    public void setSet(int i, int setId) {
        rangeCheck(i);
        ints[i] = setId;
    }

    /**
     * Checks if the given index is in range.  If not, throws an appropriate
     * runtime exception.  This method does *not* check if the index is
     * negative: It is always used immediately prior to an array access,
     * which throws an ArrayIndexOutOfBoundsException if index is negative.
     */
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * Constructs an IndexOutOfBoundsException detail message.
     * Of the many possible refactorings of the error handling code,
     * this "outlining" performs best with both server and client VMs.
     */
    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

}
