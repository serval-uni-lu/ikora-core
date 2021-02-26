package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class PreviousTestMessage extends LibraryVariable {
    public PreviousTestMessage(){
        super(new StringType("PREV TEST MESSAGE"), Format.SCALAR);
    }
}
