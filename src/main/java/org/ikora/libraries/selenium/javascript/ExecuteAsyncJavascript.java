package org.ikora.libraries.selenium.javascript;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class ExecuteAsyncJavascript extends LibraryKeyword {
    public ExecuteAsyncJavascript(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
