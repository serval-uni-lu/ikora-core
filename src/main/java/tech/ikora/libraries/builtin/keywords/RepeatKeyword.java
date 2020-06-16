package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.KeywordType;
import tech.ikora.types.StringType;

import java.util.Arrays;

public class RepeatKeyword extends LibraryKeyword {
    public RepeatKeyword(){
        super(Type.CONTROL_FLOW, Arrays.asList(
                new StringType("repeat"),
                new KeywordType("keyword")
        ));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
