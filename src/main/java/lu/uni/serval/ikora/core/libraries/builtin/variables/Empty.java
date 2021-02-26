package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class Empty extends LibraryVariable {
    public Empty(){
        super(new StringType("EMPTY"), Format.SCALAR);
    }
}
