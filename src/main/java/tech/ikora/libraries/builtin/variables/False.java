package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.model.Token;
import tech.ikora.types.BooleanType;
import tech.ikora.types.PathType;

public class False extends LibraryVariable {
    public False(){
        super(new BooleanType("FALSE"), Format.SCALAR);
    }
}
