package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class TestName extends LibraryVariable {
    public TestName(){
        super(new StringType("TEST NAME"), Format.SCALAR);
    }
}
