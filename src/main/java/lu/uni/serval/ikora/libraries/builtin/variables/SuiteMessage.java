package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class SuiteMessage extends LibraryVariable {
    public SuiteMessage(){
        super(new StringType("SUITE MESSAGE"), Format.SCALAR);
    }
}
