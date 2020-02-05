package tech.ikora.libraries.selenium.cookies;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class DeleteAllCookies extends LibraryKeyword {
    public DeleteAllCookies(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
