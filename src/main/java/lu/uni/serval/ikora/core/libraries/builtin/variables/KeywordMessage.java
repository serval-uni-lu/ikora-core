package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class KeywordMessage extends LibraryVariable {
    public KeywordMessage(){
        super(new StringType("KEYWORD MESSAGE"), Format.SCALAR);
    }
}
