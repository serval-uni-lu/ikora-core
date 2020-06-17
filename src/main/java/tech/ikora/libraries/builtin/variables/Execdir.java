package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.PathType;

public class Execdir extends LibraryVariable {
    public Execdir(){
        super(new PathType("EXECDIR"), Format.SCALAR);
    }
}
