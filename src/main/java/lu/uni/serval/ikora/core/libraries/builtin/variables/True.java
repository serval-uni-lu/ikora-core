package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.BooleanType;

public class True extends LibraryVariable {
    public True(){
        super(new BooleanType("TRUE"), Format.SCALAR);
    }
}
