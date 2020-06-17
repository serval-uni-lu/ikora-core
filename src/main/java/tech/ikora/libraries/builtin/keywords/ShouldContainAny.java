package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.DictionaryType;
import tech.ikora.types.ListType;
import tech.ikora.types.ObjectType;

public class ShouldContainAny extends LibraryKeyword {
    public ShouldContainAny(){
        super(Type.ASSERTION,
                new ObjectType("container"),
                new ListType("items"),
                new DictionaryType("configuration")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
