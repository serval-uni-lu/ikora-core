package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class KeywordStatus extends LibraryVariable {
    public KeywordStatus(){
        super(new StringType("KEYWORD STATUS"), Format.SCALAR);
    }
}
