package lu.uni.serval.ikora.core.builder;

import org.apache.commons.io.input.BOMInputStream;
import lu.uni.serval.ikora.core.model.Source;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.utils.FileUtils;

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
        this.sourceFile = sourceFile;
        this.source = this.sourceFile.getSource();


        if(this.source.isInMemory()){
            this.reader = new LineNumberReader(new StringReader(this.source.asString()));
        }
        else {
            Charset charset = FileUtils.detectCharset(this.source.asFile(), StandardCharsets.UTF_8);
            InputStreamReader input = new InputStreamReader(new FileInputStream(this.source.asFile()), charset);
            this.reader = new LineNumberReader(input);
        }
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
