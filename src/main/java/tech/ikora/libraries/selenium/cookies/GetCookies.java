package tech.ikora.libraries.selenium.cookies;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class GetCookies extends LibraryKeyword {
    public GetCookies(){
        this.type = Type.GET;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
