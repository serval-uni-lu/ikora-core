package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.model.Token;
import tech.ikora.types.PathType;
import tech.ikora.types.StringType;

public class ElementSeparator extends LibraryVariable {
    public ElementSeparator(){
        super(new StringType(":"), Format.SCALAR);
    }
}
