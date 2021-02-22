package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class SuiteName extends LibraryVariable {
    public SuiteName(){
        super(new StringType("SUITE NAME"), Format.SCALAR);
    }
}
