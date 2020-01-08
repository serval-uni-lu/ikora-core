package org.ikora.libraries.selenium.window;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class SelectWindow extends LibraryKeyword {
    public SelectWindow(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
