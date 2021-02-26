package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class PreviousTestStatus extends LibraryVariable {
    public PreviousTestStatus(){
        super(new StringType("PREV TEST STATUS"), Format.SCALAR);
    }
}
