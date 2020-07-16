package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.StringType;
import tech.ikora.types.TimeoutType;

public class Sleep extends LibraryKeyword {
    public Sleep(){
        super(Type.SYNCHRONIZATION,
                new TimeoutType("time"),
                new StringType("reason", "None")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
