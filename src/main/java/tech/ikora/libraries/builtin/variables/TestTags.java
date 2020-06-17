package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class TestTags extends LibraryVariable {
    public TestTags(){
        super(new StringType("TEST TAGS"), Format.LIST);
    }
}
