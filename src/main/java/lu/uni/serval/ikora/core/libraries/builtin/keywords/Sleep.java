package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.StringType;
import lu.uni.serval.ikora.core.types.TimeoutType;

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
