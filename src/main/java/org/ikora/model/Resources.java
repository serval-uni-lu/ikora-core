package org.ikora.model;

import java.io.File;
import java.util.List;

public class Resources {
    private Token name;
    private List<Token> arguments;
    private Token comment;
    private File file;

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
