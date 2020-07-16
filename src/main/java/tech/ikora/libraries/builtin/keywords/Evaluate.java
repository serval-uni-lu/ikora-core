package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.StringType;

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
