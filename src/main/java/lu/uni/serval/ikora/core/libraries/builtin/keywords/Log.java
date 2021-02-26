package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BooleanType;
import lu.uni.serval.ikora.core.types.LogLevelType;
import lu.uni.serval.ikora.core.types.StringType;

public class Log extends LibraryKeyword {
    public Log(){
        super(Type.LOG,
                new StringType("message"),
                new LogLevelType("level", "INFO"),
                new BooleanType("html", "False"),
                new BooleanType("console", "False"),
                new BooleanType("repr", "False"),
                new StringType("formatter", "str")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
