package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class PreviousTestName extends LibraryVariable {
    public PreviousTestName(){
        super(new StringType("PREV TEST NAME"), Format.SCALAR);
    }
}
