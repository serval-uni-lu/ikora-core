package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class PreviousTestName extends LibraryVariable {
    public PreviousTestName(){
        super(new StringType("PREV TEST NAME"), Format.SCALAR);
    }
}
