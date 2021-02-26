package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class SuiteMetadata extends LibraryVariable {
    public SuiteMetadata(){
        super(new StringType("SUITE METADATA"), Format.DICTIONARY);
    }
}
