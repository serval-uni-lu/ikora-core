package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class TestTags extends LibraryVariable {
    public TestTags(){
        super(new StringType("TEST TAGS"), Format.LIST);
    }
}
