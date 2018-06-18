package lu.uni.serval.robotframework.compiler;

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

    public boolean isEmpty() {
        return text.isEmpty();
    }

    public String[] tokenize(){
        String tokens = this.text.replaceAll("\\s\\s(\\s*)", "\t");
        return tokens.split("\t");
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
