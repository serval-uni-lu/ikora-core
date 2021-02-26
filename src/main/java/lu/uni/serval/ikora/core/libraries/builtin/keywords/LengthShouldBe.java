package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.NumberType;
import lu.uni.serval.ikora.core.types.StringType;

public class LengthShouldBe extends LibraryKeyword {
    public LengthShouldBe(){
        super(Type.ASSERTION,
                new StringType("item"),
                new NumberType("length"),
                new StringType("message", "None")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
