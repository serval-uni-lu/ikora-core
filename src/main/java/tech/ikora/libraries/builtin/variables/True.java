package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.BooleanType;

public class True extends LibraryVariable {
    public True(){
        super(new BooleanType("TRUE"), Format.SCALAR);
    }
}
