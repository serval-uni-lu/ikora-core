package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.model.Token;

public class False extends LibraryVariable {
    @Override
    public Token getName() {
        return Token.fromString("${false}");
    }
}
