package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class TestDocumentation extends LibraryVariable {
    public TestDocumentation(){
        super(new StringType("TEST DOCUMENTATION"), Format.SCALAR);
    }
}
