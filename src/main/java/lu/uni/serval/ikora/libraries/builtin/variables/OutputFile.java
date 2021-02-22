package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.PathType;

public class OutputFile extends LibraryVariable {
    public OutputFile(){
        super(new PathType("OUTPUT FILE"), Format.SCALAR);
    }
}
