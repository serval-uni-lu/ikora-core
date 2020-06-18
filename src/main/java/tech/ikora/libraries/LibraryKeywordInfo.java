package tech.ikora.libraries;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tech.ikora.types.BaseTypeList;
import tech.ikora.model.LibraryInfo;
import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BaseType;

@JsonDeserialize(using = LibraryKeywordInfoReader.class)
public class LibraryKeywordInfo extends LibraryKeyword {
    private final String name;
    private LibraryInfo library;

    public LibraryKeywordInfo(Type type, String name, BaseTypeList argumentTypes) {
        super(type, argumentTypes.toArray(new BaseType[0]));

        this.name = name;
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
    protected void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
