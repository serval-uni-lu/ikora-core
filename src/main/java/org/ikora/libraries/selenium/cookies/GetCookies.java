package org.ikora.libraries.selenium.cookies;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class GetCookies extends LibraryKeyword {
    public GetCookies(){
        this.type = Type.GET;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
