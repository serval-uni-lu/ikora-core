package org.ukwikora.libraries.builtin;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.Runtime;

public class LogToConsole extends LibraryKeyword {
    public LogToConsole(){
        this.type = Type.Log;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
