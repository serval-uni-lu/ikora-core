package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class TestDocumentation extends LibraryVariable {
    public TestDocumentation(){
        super(new StringType("TEST DOCUMENTATION"), Format.SCALAR);
    }
}
