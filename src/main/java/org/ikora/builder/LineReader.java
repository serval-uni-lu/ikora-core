package org.ikora.builder;

import org.apache.commons.io.input.BOMInputStream;
import org.ikora.model.SourceFile;
import org.ikora.utils.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class LineReader {
    private LineNumberReader reader;
    private Line current;
    private File file;
    private SourceFile sourceFile;

    public LineReader(File file) throws FileNotFoundException {
        this.file = file;

        Charset charset = FileUtils.detectCharset(file, StandardCharsets.UTF_8);
        InputStreamReader input = new InputStreamReader(new BOMInputStream(new FileInputStream(file)), charset);
        this.reader = new LineNumberReader(input);

        this.sourceFile = null;
    }

    public LineReader(Reader reader) {
        this.file = new File("/");
        this.reader = new LineNumberReader(reader);
        this.sourceFile = null;
    }

    public LineReader(SourceFile sourceFile) throws FileNotFoundException {
        this.file = sourceFile.getFile();

        Charset charset = FileUtils.detectCharset(this.file, StandardCharsets.UTF_8);
        InputStreamReader input = new InputStreamReader(new FileInputStream(this.file), charset);
        this.reader = new LineNumberReader(input);

        this.sourceFile = sourceFile;
    }

    public Line readLine() throws IOException {
        String currentText = reader.readLine();
        int currentNumber = reader.getLineNumber();

        current = new Line(currentText, currentNumber, LexerUtils.isComment(currentText), LexerUtils.isEmpty(currentText));

        if(current.isValid() && sourceFile != null){
            sourceFile.addLine(current);
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
