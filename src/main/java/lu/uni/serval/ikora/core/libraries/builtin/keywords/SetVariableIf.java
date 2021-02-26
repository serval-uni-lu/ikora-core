package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.ConditionType;
import lu.uni.serval.ikora.core.types.ListType;

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
