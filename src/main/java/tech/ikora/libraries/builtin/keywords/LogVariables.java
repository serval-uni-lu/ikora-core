package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.LogLevelType;

import java.util.Collections;

public class LogVariables extends LibraryKeyword {
    public LogVariables(){
        super(Type.LOG, Collections.singletonList(new LogLevelType("level", "INFO")));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
