package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class ElementSeparator extends LibraryVariable {
    public ElementSeparator(){
        super(new StringType(":"), Format.SCALAR);
    }
}
