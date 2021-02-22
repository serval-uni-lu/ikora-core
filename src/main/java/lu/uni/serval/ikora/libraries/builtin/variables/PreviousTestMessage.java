package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class PreviousTestMessage extends LibraryVariable {
    public PreviousTestMessage(){
        super(new StringType("PREV TEST MESSAGE"), Format.SCALAR);
    }
}
