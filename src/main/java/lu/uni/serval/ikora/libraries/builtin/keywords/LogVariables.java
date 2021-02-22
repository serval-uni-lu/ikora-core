package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.LogLevelType;

public class LogVariables extends LibraryKeyword {
    public LogVariables(){
        super(Type.LOG, new LogLevelType("level", "INFO"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
