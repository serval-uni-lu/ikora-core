package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class KeywordStatus extends LibraryVariable {
    public KeywordStatus(){
        super(new StringType("KEYWORD STATUS"), Format.SCALAR);
    }
}
