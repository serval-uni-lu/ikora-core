package tech.ikora.builder;

import org.apache.commons.io.input.BOMInputStream;
import tech.ikora.model.Source;
import tech.ikora.model.SourceFile;
import tech.ikora.utils.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class LineReader {
    private LineNumberReader reader;
    private Line current;
    private Source source;
    private SourceFile sourceFile;

    public LineReader(File file) throws FileNotFoundException {
        this.source = new Source(file);

        Charset charset = FileUtils.detectCharset(file, StandardCharsets.UTF_8);
        InputStreamReader input = new InputStreamReader(new BOMInputStream(new FileInputStream(file)), charset);
        this.reader = new LineNumberReader(input);

        this.sourceFile = null;
    }

    public LineReader(String inMemory) {
        this.source = new Source(inMemory);

        this.reader = new LineNumberReader(new StringReader(inMemory));
        this.sourceFile = null;
    }

    public LineReader(SourceFile sourceFile) throws FileNotFoundException {
        this.source = sourceFile.getSource();

        Charset charset = FileUtils.detectCharset(this.source.asFile(), StandardCharsets.UTF_8);
        InputStreamReader input = new InputStreamReader(new FileInputStream(this.source.asFile()), charset);
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

    public Source getSource() {
        return source;
    }

    void close() throws IOException {
            reader.close();
    }
}
