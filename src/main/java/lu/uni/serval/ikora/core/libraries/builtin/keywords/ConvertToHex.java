package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BooleanType;
import lu.uni.serval.ikora.core.types.NumberType;
import lu.uni.serval.ikora.core.types.StringType;

public class ConvertToHex extends LibraryKeyword {
    public ConvertToHex(){
        super(Type.SET,
                new StringType("item"),
                new NumberType("base", "None"),
                new StringType("prefix", "None"),
                new BooleanType("lowercase", "False")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
