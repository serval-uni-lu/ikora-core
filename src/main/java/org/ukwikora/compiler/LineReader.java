package org.ukwikora.compiler;

import org.apache.commons.io.input.BOMInputStream;
import org.ukwikora.model.TestCaseFile;
import org.ukwikora.utils.FileUtils;

import java.io.*;
import java.nio.charset.Charset;

public class LineReader {
    private LineNumberReader reader;
    private Line current;
    private File file;
    private TestCaseFile testCaseFile;
    private int loc;

    public LineReader(File file) throws FileNotFoundException {
        this.file = file;
        loc = 0;

        Charset charset = FileUtils.detectCharset(file,  Charset.forName("UTF-8"));
        InputStreamReader input = new InputStreamReader(new BOMInputStream(new FileInputStream(file)), charset);
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

        Charset charset = FileUtils.detectCharset(this.file, Charset.forName("UTF-8"));
        InputStreamReader input = new InputStreamReader(new FileInputStream(this.file), charset);
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
