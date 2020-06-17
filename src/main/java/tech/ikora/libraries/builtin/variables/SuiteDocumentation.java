package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class SuiteDocumentation extends LibraryVariable {
    public SuiteDocumentation(){
        super(new StringType("SUITE DOCUMENTATION"), Format.SCALAR);
    }
}
