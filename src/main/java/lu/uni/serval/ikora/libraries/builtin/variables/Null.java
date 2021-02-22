package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class Null extends LibraryVariable {
    public Null(){
        super(new StringType("NULL"), Format.SCALAR);
    }
}
