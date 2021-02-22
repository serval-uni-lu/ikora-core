package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.PathType;

public class LogFile extends LibraryVariable {
    public LogFile(){
        super(new PathType("LOG FILE"), Format.SCALAR);
    }
}
