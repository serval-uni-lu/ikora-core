package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.BooleanType;
import lu.uni.serval.ikora.types.NumberType;
import lu.uni.serval.ikora.types.StringType;

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
