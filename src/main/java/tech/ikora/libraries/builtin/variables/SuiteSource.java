package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.PathType;

public class SuiteSource extends LibraryVariable {
    public SuiteSource(){
        super(new PathType("SUITE SOURCE"), Format.SCALAR);
    }
}
