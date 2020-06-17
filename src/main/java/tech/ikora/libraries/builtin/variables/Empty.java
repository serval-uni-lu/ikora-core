package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class Empty extends LibraryVariable {
    public Empty(){
        super(new StringType("EMPTY"), Format.SCALAR);
    }
}
