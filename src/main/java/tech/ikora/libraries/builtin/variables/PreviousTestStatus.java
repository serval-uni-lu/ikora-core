package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class PreviousTestStatus extends LibraryVariable {
    public PreviousTestStatus(){
        super(new StringType("PREV TEST STATUS"), Format.SCALAR);
    }
}
