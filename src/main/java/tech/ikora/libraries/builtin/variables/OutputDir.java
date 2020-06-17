package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.PathType;

public class OutputDir extends LibraryVariable {
    public OutputDir(){
        super(new PathType("OUTPUT DIR"), Format.SCALAR);
    }
}
