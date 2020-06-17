package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class TestName extends LibraryVariable {
    public TestName(){
        super(new StringType("TEST NAME"), Format.SCALAR);
    }
}
