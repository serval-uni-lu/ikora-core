package org.ukwikora.compiler;

import org.ukwikora.model.TestCaseFile;

import java.io.*;

public class LineReader {
    private LineNumberReader reader;
    private Line current;
    private File file;
    private TestCaseFile testCaseFile;
    private int loc;

    public LineReader(File file) throws FileNotFoundException {
        this.file = file;
        loc = 0;

        FileReader input = new FileReader(this.file);
        this.reader = new LineNumberReader(input);

        this.testCaseFile = null;
    }

    public LineReader(Reader reader) {
        this.file = new File("/");
        loc = 0;

        this.reader = new LineNumberReader(reader);
        this.testCaseFile = null;
    }

    public LineReader(TestCaseFile testCaseFile) throws FileNotFoundException {
        this.file = testCaseFile.getFile();
        loc = 0;

        FileReader input = new FileReader(this.file);
        this.reader = new LineNumberReader(input);

        this.testCaseFile = testCaseFile;
    }

    public Line readLine() throws IOException {
        String currentText = reader.readLine();
        int currentNumber = reader.getLineNumber();

        current = new Line(currentText, currentNumber, LexerUtils.isComment(currentText), LexerUtils.isEmpty(currentText));

        if(testCaseFile != null){
            testCaseFile.addLine(current);
        }

        if(current.isValid() && !current.isEmpty()){
            ++loc;
        }

        if(testCaseFile != null && !current.isValid()){
            testCaseFile.setLoc(loc);
        }

        return current;
    }

    Line getCurrent(){
        return this.current;
    }

    public File getFile() {
        return file;
    }

    void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
