package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.PathType;

public class DebugFile extends LibraryVariable {
    public DebugFile(){
        super(new PathType("debug file"), Format.SCALAR);
    }
}
