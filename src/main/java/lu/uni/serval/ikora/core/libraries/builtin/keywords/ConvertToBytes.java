package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.StringType;

public class ConvertToBytes extends LibraryKeyword {
    public ConvertToBytes(){
        super(Type.SET,
                new StringType("item"),
                new StringType("input_type", "text")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
