package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.LogLevelType;

public class SetLogLevel extends LibraryKeyword {
    public SetLogLevel(){
        super(Type.SET, new LogLevelType("level"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
