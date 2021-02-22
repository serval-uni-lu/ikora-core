package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class SuiteDocumentation extends LibraryVariable {
    public SuiteDocumentation(){
        super(new StringType("SUITE DOCUMENTATION"), Format.SCALAR);
    }
}
