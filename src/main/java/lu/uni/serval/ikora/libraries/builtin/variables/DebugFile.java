package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.PathType;

public class DebugFile extends LibraryVariable {
    public DebugFile(){
        super(new PathType("DEBUG FILE"), Format.SCALAR);
    }
}
