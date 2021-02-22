package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class KeywordStatus extends LibraryVariable {
    public KeywordStatus(){
        super(new StringType("KEYWORD STATUS"), Format.SCALAR);
    }
}
