package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ConditionType;
import tech.ikora.types.ListType;

public class SetVariableIf extends LibraryKeyword {
    public SetVariableIf(){
        super(Type.SET,
                new ConditionType("condition"),
                new ListType("values")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
