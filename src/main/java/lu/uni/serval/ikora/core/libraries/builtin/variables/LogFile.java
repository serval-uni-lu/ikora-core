package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.PathType;

public class LogFile extends LibraryVariable {
    public LogFile(){
        super(new PathType("LOG FILE"), Format.SCALAR);
    }
}
