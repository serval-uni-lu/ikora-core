package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.BooleanType;
import lu.uni.serval.ikora.types.ObjectType;
import lu.uni.serval.ikora.types.StringType;

public class ShouldNotContain extends LibraryKeyword {
    public ShouldNotContain(){
        super(Type.ASSERTION,
                new ObjectType("container"),
                new StringType("item"),
                new StringType("message", "None"),
                new BooleanType("values", "True"),
                new BooleanType("ignore_case", "False")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
