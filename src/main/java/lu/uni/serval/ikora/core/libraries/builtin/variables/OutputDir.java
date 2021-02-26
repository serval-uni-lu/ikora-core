package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.PathType;

public class OutputDir extends LibraryVariable {
    public OutputDir(){
        super(new PathType("OUTPUT DIR"), Format.SCALAR);
    }
}
