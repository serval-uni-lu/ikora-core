package tech.ikora.model;

public class Pointer {
    private int line;
    private int lineOffset;

    public Pointer(int line, int lineOffset){
        this.line = line;
        this.lineOffset = lineOffset;
    }

    public int getLine() {
        return line;
    }

    public int getLineOffset() {
        return lineOffset;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", line, lineOffset);
    }
}
