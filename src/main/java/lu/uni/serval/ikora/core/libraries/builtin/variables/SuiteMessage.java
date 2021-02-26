package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class SuiteMessage extends LibraryVariable {
    public SuiteMessage(){
        super(new StringType("SUITE MESSAGE"), Format.SCALAR);
    }
}
