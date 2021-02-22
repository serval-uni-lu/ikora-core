package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class KeywordMessage extends LibraryVariable {
    public KeywordMessage(){
        super(new StringType("KEYWORD MESSAGE"), Format.SCALAR);
    }
}
