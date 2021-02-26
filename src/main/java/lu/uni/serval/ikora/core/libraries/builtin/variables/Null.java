package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class Null extends LibraryVariable {
    public Null(){
        super(new StringType("NULL"), Format.SCALAR);
    }
}
