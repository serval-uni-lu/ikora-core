package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.model.Token;
import tech.ikora.types.StringType;

public class LineSeparator extends LibraryVariable {
    public LineSeparator(){
        super(new StringType("\\n"), Format.SCALAR);
    }
}
