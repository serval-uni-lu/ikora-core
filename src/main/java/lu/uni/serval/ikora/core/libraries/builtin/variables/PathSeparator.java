package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class PathSeparator extends LibraryVariable {
    public PathSeparator(){
        super(new StringType("/"), Format.SCALAR);
    }
}
