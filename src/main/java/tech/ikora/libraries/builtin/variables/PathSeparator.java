package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.model.Token;
import tech.ikora.types.StringType;

public class PathSeparator extends LibraryVariable {
    public PathSeparator(){
        super(new StringType("/"), Format.SCALAR);
    }
}
