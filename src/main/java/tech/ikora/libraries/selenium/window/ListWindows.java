package tech.ikora.libraries.selenium.window;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class ListWindows extends LibraryKeyword {
    public ListWindows(){
        this.type = Type.GET;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}