package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class TestTags extends LibraryVariable {
    public TestTags(){
        super(new StringType("TEST TAGS"), Format.LIST);
    }
}
