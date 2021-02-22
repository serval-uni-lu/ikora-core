package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.LogLevelType;

public class SetLogLevel extends LibraryKeyword {
    public SetLogLevel(){
        super(Type.CONFIGURATION, new LogLevelType("level"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
