package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.BooleanType;
import lu.uni.serval.ikora.types.StringType;

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
