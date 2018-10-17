package lu.uni.serval.robotframework.libraries.builtin.variables;

import lu.uni.serval.robotframework.model.LibraryVariable;

import java.util.regex.Pattern;

public class Space extends LibraryVariable {
    private Pattern match;

    public Space(){
        match = Pattern.compile("^\\$\\{SPACE(\\s*\\*\\s*\\d*)?\\}$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean matches(String name) {
        return match.matcher(name).matches();
    }
}
