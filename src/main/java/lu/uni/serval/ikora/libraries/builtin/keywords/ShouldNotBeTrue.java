package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.ConditionType;
import lu.uni.serval.ikora.types.StringType;

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
