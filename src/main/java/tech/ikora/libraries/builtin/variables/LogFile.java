package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.PathType;

public class LogFile extends LibraryVariable {
    public LogFile(){
        super(new PathType("LOG FILE"), Format.SCALAR);
    }
}
