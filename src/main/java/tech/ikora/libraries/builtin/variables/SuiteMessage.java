package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class SuiteMessage extends LibraryVariable {
    public SuiteMessage(){
        super(new StringType("SUITE MESSAGE"), Format.SCALAR);
    }
}
