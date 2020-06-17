package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BooleanType;
import tech.ikora.types.ObjectType;
import tech.ikora.types.StringType;

public class ShouldContain extends LibraryKeyword {
    public ShouldContain(){
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
