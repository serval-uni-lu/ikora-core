package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class TestMessage extends LibraryVariable {
    public TestMessage(){
        super(new StringType("TEST MESSAGE"), Format.SCALAR);
    }
}
