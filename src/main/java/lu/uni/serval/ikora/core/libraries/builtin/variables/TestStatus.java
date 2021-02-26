package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class TestStatus extends LibraryVariable {
    public TestStatus(){
        super(new StringType("TEST STATUS"), Format.SCALAR);
    }
}
