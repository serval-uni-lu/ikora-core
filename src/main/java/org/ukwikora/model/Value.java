package org.ukwikora.model;

import org.ukwikora.analytics.Action;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Value implements Differentiable {
    public enum Type{
        String, Object, Keyword, Locator, Condition, Keywords, Kwargs
    }

    private enum Matching{
        isVariable, hasVariable, findVariable
    }

    private static Pattern isVariablePattern;
    private static Pattern hasVariablePattern;
    private static Pattern findVariablePattern;
    private static Pattern escapePattern;

    static {
        isVariablePattern = Pattern.compile("^(([@$&])\\{)(.*?)(})$");
        hasVariablePattern = Pattern.compile("(.*)(([@$&])\\{)(.*?)(})(.*)");
        findVariablePattern = Pattern.compile("(([@$&])\\{)(.*?)(})");
        escapePattern = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");
    }

    private String value;
    private Pattern match;
    private Map<String, Variable> variables;

    Value(String value) {
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

    @Override
    public String getName(){
        return toString();
    }

    public boolean matches(String string) {
        Matcher matcher = match.matcher(string);
        return matcher.matches();
    }

    public boolean isVariable() {
        Matcher matcher = getVariableMatcher(this.value, Matching.isVariable);
        return matcher.matches();
    }

    public static boolean isVariable(String text) {
        Value value = new Value(text);
        return value.isVariable();
    }

    public boolean hasVariable() {
        Matcher matcher = getVariableMatcher(this.value, Matching.hasVariable);
        return matcher.matches();
    }

    public static boolean hasVariable(String text) {
        Value value = new Value(text);
        return value.hasVariable();
    }

    public List<String> findVariables() {
        List<String> variables = new ArrayList<>();

        Matcher matcher = getVariableMatcher(this.value, Matching.findVariable);

        while (matcher.find()){
            variables.add(this.value.substring(matcher.start(), matcher.end()));
        }

        return variables;
    }

    public static List<String> findVariables(String text) {
        Value value = new Value(text);
        return value.findVariables();
    }

    public Optional<List<Value>> getResolvedValues() {
        if(isVariable()){
            return this.variables.get(this.value).getResolvedValues();
        }

        if(hasVariable()){
            String resolvedValue = this.value;

            for(Map.Entry<String, Variable> entry: this.variables.entrySet()){
                resolvedValue = resolvedValue.replace(entry.getKey(), entry.getValue().getValueAsString());
            }

            return Optional.of(Collections.singletonList(new Value(resolvedValue)));
        }

        return Optional.of(Collections.singletonList(this));
    }

    private void buildRegex() {
        Matcher matcher = getVariableMatcher(this.value, Matching.findVariable);

        String placeholder = "@@@@___VARIABLE__PLACEHOLDER___@@@@";

        String pattern = matcher.replaceAll(placeholder).trim();
        pattern = escape(pattern);
        pattern = pattern.replaceAll(placeholder, "(.*)");

        match = Pattern.compile("^" + pattern + "$", Pattern.CASE_INSENSITIVE);
    }

    private static Matcher getVariableMatcher(String value, Matching matching) {
        Matcher matcher = null;

        switch (matching) {
            case isVariable:
                matcher = isVariablePattern.matcher(value);
                break;
            case hasVariable:
                matcher = hasVariablePattern.matcher(value);
                break;
            case findVariable:
                matcher = findVariablePattern.matcher(value);
                break;
        }

        return matcher;
    }

    public static String escape(String s){
        return escapePattern.matcher(s).replaceAll("\\\\$0");
    }

    @Override
    public double distance(@Nonnull Differentiable other) {
        if(other == this){
            return 0.0;
        }

        if(!(other instanceof Value)){
            return 1;
        }

        Value value = (Value)other;
        return this.value.equals(value.value) ? 0 : 1;
    }

    @Override
    public List<Action> differences(@Nonnull Differentiable other) {
        return null;
    }

    public static Value empty(){
        return new Value("");
    }
}
