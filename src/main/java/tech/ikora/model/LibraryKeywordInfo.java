package tech.ikora.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tech.ikora.runner.Runtime;

@JsonDeserialize(using = LibraryKeywordInfoReader.class)
public class LibraryKeywordInfo extends LibraryKeyword {
    private final Type type;
    private final String name;
    private final Argument.Type[] argumentTypes;

    private LibraryInfo library;

    public LibraryKeywordInfo(Type type, String name, Argument.Type[] argumentTypes) {
        this.type = type;
        this.name = name;
        this.argumentTypes = argumentTypes;
        this.library = null;
    }

    public LibraryInfo getLibrary() {
        return library;
    }

    public void setLibrary(LibraryInfo library) {
        this.library = library;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return argumentTypes;
    }

    @Override
    protected void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
