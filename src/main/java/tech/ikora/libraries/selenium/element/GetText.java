package tech.ikora.libraries.selenium.element;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class GetText extends LibraryKeyword {
    public GetText(){
        this.type = Type.GET;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}