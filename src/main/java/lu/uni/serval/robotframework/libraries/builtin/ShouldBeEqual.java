package lu.uni.serval.robotframework.libraries.builtin;

import lu.uni.serval.robotframework.model.Value;
import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class ShouldBeEqual extends LibraryKeyword {
    public ShouldBeEqual(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[]{
                Value.Type.String,
                Value.Type.String
        };
    }
}
