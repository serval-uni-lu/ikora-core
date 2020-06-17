package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class SuiteStatus extends LibraryVariable {
    public SuiteStatus(){
        super(new StringType("SUITE STATUS"), Format.SCALAR);
    }
}
