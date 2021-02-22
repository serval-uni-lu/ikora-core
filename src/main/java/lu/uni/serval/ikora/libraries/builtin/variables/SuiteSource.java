package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.PathType;

public class SuiteSource extends LibraryVariable {
    public SuiteSource(){
        super(new PathType("SUITE SOURCE"), Format.SCALAR);
    }
}
