package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.BooleanType;

public class False extends LibraryVariable {
    public False(){
        super(new BooleanType("FALSE"), Format.SCALAR);
    }
}
