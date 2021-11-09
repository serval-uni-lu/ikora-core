package lu.uni.serval.ikora.core.exception;

public class OutOfRangeException extends RuntimeException {
    private final int index;
    private final int lower;
    private final int upper;

    public OutOfRangeException(int index, int lower, int upper) {
        super(String.format("Index %d is out of bound: [%d,%d]", index, lower, upper));

        this.index = index;
        this.lower = lower;
        this.upper = upper;
    }

    public int getIndex() {
        return index;
    }

    public int getLower() {
        return lower;
    }

    public int getUpper() {
        return upper;
    }
}
