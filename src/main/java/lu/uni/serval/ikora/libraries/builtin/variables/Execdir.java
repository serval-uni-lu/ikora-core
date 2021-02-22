package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.PathType;

public class Execdir extends LibraryVariable {
    public Execdir(){
        super(new PathType("EXECDIR"), Format.SCALAR);
    }
}
