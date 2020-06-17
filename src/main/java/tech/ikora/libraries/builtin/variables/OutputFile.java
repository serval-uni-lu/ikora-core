package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.PathType;

public class OutputFile extends LibraryVariable {
    public OutputFile(){
        super(new PathType("OUTPUT FILE"), Format.SCALAR);
    }
}
