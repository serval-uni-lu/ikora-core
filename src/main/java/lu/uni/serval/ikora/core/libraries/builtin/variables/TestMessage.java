package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class TestMessage extends LibraryVariable {
    public TestMessage(){
        super(new StringType("TEST MESSAGE"), Format.SCALAR);
    }
}
