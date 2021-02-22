package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class TestStatus extends LibraryVariable {
    public TestStatus(){
        super(new StringType("TEST STATUS"), Format.SCALAR);
    }
}
