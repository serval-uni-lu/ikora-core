package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class SuiteName extends LibraryVariable {
    public SuiteName(){
        super(new StringType("SUITE NAME"), Format.SCALAR);
    }
}
