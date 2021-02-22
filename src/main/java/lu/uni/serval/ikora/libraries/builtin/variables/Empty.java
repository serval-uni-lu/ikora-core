package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class Empty extends LibraryVariable {
    public Empty(){
        super(new StringType("EMPTY"), Format.SCALAR);
    }
}
