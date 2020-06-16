package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BooleanType;
import tech.ikora.types.StringType;

import java.util.Arrays;

public class GetLibraryInstance extends LibraryKeyword {
    public GetLibraryInstance(){
        super(Type.UNKNOWN, Arrays.asList(
                new StringType("name", "None"),
                new BooleanType("all", "False")
        ));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
