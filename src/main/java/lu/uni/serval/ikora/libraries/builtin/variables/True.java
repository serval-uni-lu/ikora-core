package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.BooleanType;

public class True extends LibraryVariable {
    public True(){
        super(new BooleanType("TRUE"), Format.SCALAR);
    }
}
