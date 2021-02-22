package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.PathType;

public class OutputDir extends LibraryVariable {
    public OutputDir(){
        super(new PathType("OUTPUT DIR"), Format.SCALAR);
    }
}
