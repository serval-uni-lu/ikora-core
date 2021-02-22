package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.NumberType;
import lu.uni.serval.ikora.types.StringType;

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
