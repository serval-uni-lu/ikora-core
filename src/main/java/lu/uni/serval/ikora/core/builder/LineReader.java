package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.model.Source;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.utils.FileUtils;

import java.io.*;

public class LineReader {
    private final LineNumberReader reader;
    private final Source source;
    private final SourceFile sourceFile;

    private Line current;

    public LineReader(File file) throws IOException {
        this.source = new Source(file);
        this.reader = new LineNumberReader(FileUtils.getUnicodeReader(file));
        this.sourceFile = null;
    }

    public LineReader(String inMemory) {
        this.source = new Source(inMemory);

        this.reader = new LineNumberReader(new StringReader(inMemory));
        this.sourceFile = null;
    }

    public LineReader(SourceFile sourceFile) throws IOException {
        this.sourceFile = sourceFile;
        this.source = this.sourceFile.getSource();


        if(this.source.isInMemory()){
            this.reader = new LineNumberReader(new StringReader(this.source.asString()));
        }
        else {
            this.reader = new LineNumberReader(FileUtils.getUnicodeReader(this.source.asFile()));
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
