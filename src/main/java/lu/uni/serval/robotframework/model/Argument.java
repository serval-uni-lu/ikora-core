package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Argument {
    private String value;
    private Pattern match;
    private Map<String, Variable> variables;

    Argument(String value) {
        this.value = value;
        this.variables = new HashMap<>();

        buildRegex();
    }

    public void setVariable(String name, Variable value) {
        this.variables.put(name, value);
    }

    @Override
    public String toString() {
        return this.value;
    }

    public boolean matches(String string) {
        Matcher matcher = match.matcher(string);
        return matcher.matches();
    }

    public boolean hasVariable() {
        Matcher matcher = getVariableMatcher(this.value);
        return matcher.matches();
    }

    static public boolean hasVariable(String value) {
        Argument argument = new Argument(value);
        return argument.hasVariable();
    }

    public List<String> findVariables() {
        List<String> variables = new ArrayList<>();

        Matcher matcher = getVariableMatcher(this.value);

        while (matcher.find()){
            variables.add(this.value.substring(matcher.start(), matcher.end()));
        }

        return variables;
    }

    public static List<String> findVariables(String value) {
        Argument argument = new Argument(value);
        return argument.findVariables();
    }

    private void buildRegex() {
        Matcher matcher = getVariableMatcher(this.value);
        String pattern = matcher.replaceAll("(.*)").trim();

        match = Pattern.compile("^" + pattern + "$", Pattern.CASE_INSENSITIVE);
    }

    public static Matcher getVariableMatcher(String value) {
        return Pattern.compile("\\$\\{(.*?)\\}").matcher(value);
    }
}
