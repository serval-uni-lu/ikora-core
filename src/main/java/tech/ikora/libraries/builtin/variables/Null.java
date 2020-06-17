package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class Null extends LibraryVariable {
    public Null(){
        super(new StringType("NULL"), Format.SCALAR);
    }
}
