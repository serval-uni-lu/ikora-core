package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.ConditionType;
import lu.uni.serval.ikora.core.types.StringType;

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
