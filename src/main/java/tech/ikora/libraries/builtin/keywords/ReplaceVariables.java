package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.LogLevelType;
import tech.ikora.types.StringType;

import java.util.Collections;

public class ReplaceVariables extends LibraryKeyword {
    public ReplaceVariables(){
        super(Type.SET, new StringType("text"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
