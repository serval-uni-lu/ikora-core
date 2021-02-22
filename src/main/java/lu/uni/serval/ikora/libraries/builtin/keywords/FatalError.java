package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.StringType;

public class FatalError extends LibraryKeyword {
    public FatalError(){
        super(Type.ERROR, new StringType("message", "None"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
