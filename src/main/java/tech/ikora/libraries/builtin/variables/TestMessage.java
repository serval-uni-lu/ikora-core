package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class TestMessage extends LibraryVariable {
    public TestMessage(){
        super(new StringType("TEST MESSAGE"), Format.SCALAR);
    }
}
