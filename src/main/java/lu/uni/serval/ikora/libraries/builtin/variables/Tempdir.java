package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.PathType;

public class Tempdir extends LibraryVariable {
    public Tempdir(){
        super(new PathType("TEMP DIR"), Format.SCALAR);
    }
}
