package lu.uni.serval.robotframework.libraries.builtin;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class Log extends LibraryKeyword {
    public Log(){
        this.type = Type.Log;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
