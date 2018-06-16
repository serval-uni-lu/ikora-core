package lu.uni.serval.robotframework.parser;

import java.io.IOException;
import java.io.LineNumberReader;

public class Line {
    private String text;
    private int number;

    private Line() {
        this.number = 0;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    public boolean isValid() {
        return text != null;
    }

    public static Line getNextLine(LineNumberReader reader) throws IOException {
        Line line = new Line();

        line.text = reader.readLine();

        if(line.text != null) {
            line.number = reader.getLineNumber();
        }

        return line;
    }
}
