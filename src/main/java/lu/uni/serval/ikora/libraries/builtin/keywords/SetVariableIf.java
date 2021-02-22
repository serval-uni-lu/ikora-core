package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.ConditionType;
import lu.uni.serval.ikora.types.ListType;

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
