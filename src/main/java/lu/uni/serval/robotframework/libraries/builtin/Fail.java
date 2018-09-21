package lu.uni.serval.robotframework.libraries.builtin;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class Fail extends LibraryKeyword  {
    public Fail(){
        this.type = Type.Error;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
