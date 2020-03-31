package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.model.Token;

public class False extends LibraryVariable {
    @Override
    public String getName() {
        return "${false}";
    }
}
