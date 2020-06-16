package tech.ikora.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BaseType;

import java.util.List;

@JsonDeserialize(using = LibraryKeywordInfoReader.class)
public class LibraryKeywordInfo extends LibraryKeyword {
    private final Type type;
    private final String name;
    private final List<? extends BaseType> argumentTypes;

    private LibraryInfo library;

    public LibraryKeywordInfo(Type type, String name, List<? extends BaseType> argumentTypes) {
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
    public List<? extends BaseType> getArgumentTypes() {
        return argumentTypes;
    }

    @Override
    protected void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
