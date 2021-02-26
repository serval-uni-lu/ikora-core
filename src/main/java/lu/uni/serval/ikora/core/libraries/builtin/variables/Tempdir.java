package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.PathType;

public class Tempdir extends LibraryVariable {
    public Tempdir(){
        super(new PathType("TEMP DIR"), Format.SCALAR);
    }
}
