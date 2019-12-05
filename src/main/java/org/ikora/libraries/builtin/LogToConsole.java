package org.ikora.libraries.builtin;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class LogToConsole extends LibraryKeyword {
    public LogToConsole(){
        this.type = Type.Log;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
