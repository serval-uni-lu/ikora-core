package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class SuiteMetadata extends LibraryVariable {
    public SuiteMetadata(){
        super(new StringType("SUITE METADATA"), Format.DICTIONARY);
    }
}
