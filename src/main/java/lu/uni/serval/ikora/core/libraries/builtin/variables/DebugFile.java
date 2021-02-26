package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.PathType;

public class DebugFile extends LibraryVariable {
    public DebugFile(){
        super(new PathType("DEBUG FILE"), Format.SCALAR);
    }
}
