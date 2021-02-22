package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class PreviousTestStatus extends LibraryVariable {
    public PreviousTestStatus(){
        super(new StringType("PREV TEST STATUS"), Format.SCALAR);
    }
}
