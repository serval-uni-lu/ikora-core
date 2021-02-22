package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.StringType;
import lu.uni.serval.ikora.types.TimeoutType;

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
