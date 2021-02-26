package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class SuiteStatus extends LibraryVariable {
    public SuiteStatus(){
        super(new StringType("SUITE STATUS"), Format.SCALAR);
    }
}
