package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.PathType;

public class Curdir extends LibraryVariable {
    public Curdir(){
        super(new PathType("CURDIR"), Format.SCALAR);
    }
}
