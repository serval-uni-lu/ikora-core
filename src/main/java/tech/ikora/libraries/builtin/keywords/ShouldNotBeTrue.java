package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ConditionType;
import tech.ikora.types.StringType;

public class ShouldNotBeTrue extends LibraryKeyword {
    public ShouldNotBeTrue(){
        super(Type.ASSERTION,
                new ConditionType("condition"),
                new StringType("message", "None")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
