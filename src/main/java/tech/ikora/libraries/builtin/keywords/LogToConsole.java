package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BooleanType;
import tech.ikora.types.StringType;

public class LogToConsole extends LibraryKeyword {
    public LogToConsole(){
        super(Type.LOG,
                new StringType("message"),
                new StringType("stream", "STDOUT"),
                new BooleanType("no_newline", "False")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
