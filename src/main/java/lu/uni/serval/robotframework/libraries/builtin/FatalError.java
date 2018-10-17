package lu.uni.serval.robotframework.libraries.builtin;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class FatalError extends LibraryKeyword {
    public FatalError(){
        this.type = Type.Error;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
