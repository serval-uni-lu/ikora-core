package tech.ikora.libraries.selenium.alert;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class GetAlertMessage extends LibraryKeyword {
    public GetAlertMessage(){
        this.type = Type.GET;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
