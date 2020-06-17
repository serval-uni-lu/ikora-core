package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.model.Token;
import tech.ikora.types.StringType;

import java.util.regex.Pattern;

public class Space extends LibraryVariable {
    private Pattern match;

    public Space(){
        super(new StringType("SPACE"), Format.SCALAR);
        match = Pattern.compile("^\\$\\{SPACE(\\s*\\*\\s*\\d*)?}$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean matches(Token name) {
        return match.matcher(name.getText()).matches();
    }
}
