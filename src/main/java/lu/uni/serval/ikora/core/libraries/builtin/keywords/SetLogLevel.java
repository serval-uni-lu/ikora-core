package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.LogLevelType;

public class SetLogLevel extends LibraryKeyword {
    public SetLogLevel(){
        super(Type.CONFIGURATION, new LogLevelType("level"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
