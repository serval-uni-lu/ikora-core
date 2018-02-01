package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Argument {
    private String value;

    Argument(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public boolean hasVariable() {
        return value.matches(".*\\$\\{.*?\\}.*");
    }

    static public boolean hasVariable(String value) {
        Argument argument = new Argument(value);
        return argument.hasVariable();
    }

    public List<String> findVariables() {
        List<String> variables = new ArrayList<String>();

        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(this.value);

        while (matcher.find()){
            variables.add(this.value.substring(matcher.start(), matcher.end()));
        }

        return variables;
    }

    public static List<String> findVariables(String value) {
        Argument argument = new Argument(value);
        return argument.findVariables();
    }
}
