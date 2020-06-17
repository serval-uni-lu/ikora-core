package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;

public class RunKeywords extends LibraryKeyword {
    public RunKeywords(){
        super(Type.CONTROL_FLOW, new ListType("keywords"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
