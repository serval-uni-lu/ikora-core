package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.BooleanType;

public class False extends LibraryVariable {
    public False(){
        super(new BooleanType("FALSE"), Format.SCALAR);
    }
}
