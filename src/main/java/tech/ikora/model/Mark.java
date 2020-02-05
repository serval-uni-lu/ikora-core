package tech.ikora.model;

public class Mark {
    private int line;
    private int column;

    public Mark(int line, int column){
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", line, column);
    }

    public boolean before(Mark other) {
        if(this == other){
            return true;
        }

        if(this.line == other.line){
            return this.column < other.column;
        }

        return this.line < other.line;
    }

    public boolean after(Mark other) {
        if(this == other){
            return true;
        }

        if(this.line == other.line){
            return this.column > other.column;
        }

        return this.line > other.line;
    }
}
