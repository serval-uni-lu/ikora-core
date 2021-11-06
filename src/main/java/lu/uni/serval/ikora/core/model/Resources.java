package lu.uni.serval.ikora.core.model;

import java.io.File;
import java.util.List;

public class Resources {
    private final Token name;
    private final List<Token> arguments;
    private final Token comment;
    private final File file;

    private SourceFile sourceFile;

    public Resources(Token name, File file, List<Token> arguments, Token comment) {
        this.name = name;
        this.arguments = arguments;
        this.comment = comment;
        this.file = file;
    }

    public Token getName() {
        return this.name;
    }

    public File getFile() {
        return file;
    }

    public SourceFile getSourceFile() {
        return this.sourceFile;
    }

    public void setSourceFile(SourceFile sourceFile) {
        this.sourceFile = sourceFile;
    }

    public boolean isValid() {
        return file != null && sourceFile != null;
    }
}
