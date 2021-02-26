package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class SuiteDocumentation extends LibraryVariable {
    public SuiteDocumentation(){
        super(new StringType("SUITE DOCUMENTATION"), Format.SCALAR);
    }
}
