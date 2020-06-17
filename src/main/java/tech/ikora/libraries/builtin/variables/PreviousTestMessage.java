package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class PreviousTestMessage extends LibraryVariable {
    public PreviousTestMessage(){
        super(new StringType("PREV TEST MESSAGE"), Format.SCALAR);
    }
}
