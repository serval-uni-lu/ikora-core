package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class PreviousTestName extends LibraryVariable {
    public PreviousTestName(){
        super(new StringType("PREV TEST NAME"), Format.SCALAR);
    }
}
