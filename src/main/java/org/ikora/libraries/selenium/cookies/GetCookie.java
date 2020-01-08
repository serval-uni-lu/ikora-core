package org.ikora.libraries.selenium.cookies;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class GetCookie extends LibraryKeyword {
    public GetCookie(){
        this.type = Type.GET;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
