package lu.uni.serval.robotframework.model;

import lu.uni.serval.analytics.Action;
import lu.uni.serval.utils.Differentiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Argument implements Differentiable {
    public enum Type{
        String, Object, Keyword, Locator, Condition, Keywords, Kwargs
    }

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

    public boolean isVariable() {
        Matcher matcher = getVariableMatcher(this.value, true);
        return matcher.matches();
    }

    public static boolean isVariable(String value) {
        Argument argument = new Argument(value);
        return argument.hasVariable();
    }

    public boolean hasVariable() {
        Matcher matcher = getVariableMatcher(this.value, false);
        return matcher.matches();
    }

    static public boolean hasVariable(String value) {
        Argument argument = new Argument(value);
        return argument.hasVariable();
    }

    public List<String> findVariables() {
        List<String> variables = new ArrayList<>();

        Matcher matcher = getVariableMatcher(this.value, false);

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
        Matcher matcher = getVariableMatcher(this.value, false);
        String placeholder = "@@@@___VARIABLE__PLACEHOLDER___@@@@";

        String pattern = matcher.replaceAll(placeholder).trim();
        pattern = escape(pattern);
        pattern = pattern.replaceAll(placeholder, "(.*)");

        match = Pattern.compile("^" + pattern + "$", Pattern.CASE_INSENSITIVE);
    }

    public static Matcher getVariableMatcher(String value, boolean strict) {
        if(strict){
            return Pattern.compile("^(([@$&])\\{)(.*?)(})$").matcher(value);
        }

        return Pattern.compile("(([@$&])\\{)(.*?)(})").matcher(value);
    }

    public static String escape(String s){
        Pattern specialRegexChars = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");
        return specialRegexChars.matcher(s).replaceAll("\\\\$0");
    }

    @Override
    public double distance(Differentiable other) {
        if(!(other instanceof Argument)){
            return 1;
        }

        Argument argument = (Argument)other;
        return value.equals(argument.value) ? 0 : 1;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        return null;
    }
}
