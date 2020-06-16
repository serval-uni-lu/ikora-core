package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.StringType;

import java.util.Arrays;

public class GetTime extends LibraryKeyword {
    public GetTime(){
        super(Type.UNKNOWN, Arrays.asList(
                new StringType("format", "timestamp"),
                new StringType("time_", "NOW")
        ));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
