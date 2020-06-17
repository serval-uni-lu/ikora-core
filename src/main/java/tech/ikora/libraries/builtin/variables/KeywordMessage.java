package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class KeywordMessage extends LibraryVariable {
    public KeywordMessage(){
        super(new StringType("KEYWORD MESSAGE"), Format.SCALAR);
    }
}
