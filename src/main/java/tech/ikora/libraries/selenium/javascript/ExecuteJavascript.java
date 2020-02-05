package tech.ikora.libraries.selenium.javascript;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class ExecuteJavascript extends LibraryKeyword {
    public ExecuteJavascript(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
