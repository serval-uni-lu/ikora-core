package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.PathType;

public class Tempdir extends LibraryVariable {
    public Tempdir(){
        super(new PathType("TEMP DIR"), Format.SCALAR);
    }
}
