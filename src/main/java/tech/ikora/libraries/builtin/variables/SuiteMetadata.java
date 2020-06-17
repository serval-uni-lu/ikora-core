package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class SuiteMetadata extends LibraryVariable {
    public SuiteMetadata(){
        super(new StringType("SUITE METADATA"), Format.DICTIONARY);
    }
}
