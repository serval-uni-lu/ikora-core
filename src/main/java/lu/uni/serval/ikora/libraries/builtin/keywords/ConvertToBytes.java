package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.StringType;

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
