package org.ikora.libraries.selenium.element;

import org.ikora.model.Argument;
import org.ikora.model.Value;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class ClickLink extends LibraryKeyword {
    public ClickLink(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[]{
                Argument.Type.LOCATOR
        };
    }
}
