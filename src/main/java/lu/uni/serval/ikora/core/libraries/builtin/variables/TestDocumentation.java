package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class TestDocumentation extends LibraryVariable {
    public TestDocumentation(){
        super(new StringType("TEST DOCUMENTATION"), Format.SCALAR);
    }
}
