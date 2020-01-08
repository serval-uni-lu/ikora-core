package org.ikora.libraries.selenium.window;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class ListWindows extends LibraryKeyword {
    public ListWindows(){
        this.type = Type.GET;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
