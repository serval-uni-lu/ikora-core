package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.PathType;

public class Execdir extends LibraryVariable {
    public Execdir(){
        super(new PathType("EXECDIR"), Format.SCALAR);
    }
}
