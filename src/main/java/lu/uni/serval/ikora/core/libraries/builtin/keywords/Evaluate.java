package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.StringType;

public class Evaluate extends LibraryKeyword {
    public Evaluate(){
        super(Type.SET,
                new StringType("expression"),
                new StringType("modules", "None"),
                new StringType("namespace", "None")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
