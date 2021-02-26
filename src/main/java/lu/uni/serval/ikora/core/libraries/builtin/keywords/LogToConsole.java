package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BooleanType;
import lu.uni.serval.ikora.core.types.StringType;

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
