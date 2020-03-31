package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.model.Token;

public class LineSeparator extends LibraryVariable {
    @Override
    public String getName() {
        return "${\\n}";
    }
}
