package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class ElementSeparator extends LibraryVariable {
    public ElementSeparator(){
        super(new StringType(":"), Format.SCALAR);
    }
}
