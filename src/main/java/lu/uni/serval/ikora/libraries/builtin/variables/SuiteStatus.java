package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class SuiteStatus extends LibraryVariable {
    public SuiteStatus(){
        super(new StringType("SUITE STATUS"), Format.SCALAR);
    }
}
