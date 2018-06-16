package lu.uni.serval.robotframework.parser;

public class Line {
    private String text;
    private int lineNumber;

    public Line(int lineNumber, String text) {
        this.text = text;
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getText() {
        return text;
    }
}
