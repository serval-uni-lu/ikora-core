package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class SuiteName extends LibraryVariable {
    public SuiteName(){
        super(new StringType("SUITE NAME"), Format.SCALAR);
    }
}
