package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.PathType;

public class SuiteSource extends LibraryVariable {
    public SuiteSource(){
        super(new PathType("SUITE SOURCE"), Format.SCALAR);
    }
}
