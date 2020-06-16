package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BooleanType;
import tech.ikora.types.LogLevelType;
import tech.ikora.types.StringType;

import java.util.Arrays;

public class Log extends LibraryKeyword {
    public Log(){
        super(Type.LOG, Arrays.asList(
                new StringType("message"),
                new LogLevelType("level", "INFO"),
                new BooleanType("html", "False"),
                new BooleanType("console", "False"),
                new BooleanType("repr", "False"),
                new StringType("formatter", "str")
        ));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
