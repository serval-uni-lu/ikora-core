package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class TestName extends LibraryVariable {
    public TestName(){
        super(new StringType("TEST NAME"), Format.SCALAR);
    }
}
