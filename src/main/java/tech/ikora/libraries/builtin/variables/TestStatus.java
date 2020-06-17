package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class TestStatus extends LibraryVariable {
    public TestStatus(){
        super(new StringType("TEST STATUS"), Format.SCALAR);
    }
}
