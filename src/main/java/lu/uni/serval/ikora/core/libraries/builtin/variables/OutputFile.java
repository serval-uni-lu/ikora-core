package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.PathType;

public class OutputFile extends LibraryVariable {
    public OutputFile(){
        super(new PathType("OUTPUT FILE"), Format.SCALAR);
    }
}
