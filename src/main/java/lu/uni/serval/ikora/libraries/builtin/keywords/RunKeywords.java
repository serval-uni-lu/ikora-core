package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.ListType;

public class RunKeywords extends LibraryKeyword {
    public RunKeywords(){
        super(Type.CONTROL_FLOW, new ListType("keywords"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
