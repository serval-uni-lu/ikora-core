package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.PathType;

public class Curdir extends LibraryVariable {
    public Curdir(){
        super(new PathType("CURDIR"), Format.SCALAR);
    }
}
