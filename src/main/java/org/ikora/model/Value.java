package org.ikora.model;

import org.ikora.analytics.Action;
import org.ikora.exception.InvalidDependencyException;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Value implements Differentiable {
    public enum Type{
        STRING, OBJECT, KEYWORD, LOCATOR, CONDITION, KEYWORDS, KWARGS
    }

    private enum Matching{
        IS_VARIABLE, HAS_VARIABLE, FIND_VARIABLE
    }

    private static Pattern isVariablePattern;
    private static Pattern hasVariablePattern;
    private static Pattern findVariablePattern;
    private static Pattern escapePattern;

    static {
        isVariablePattern = Pattern.compile("^(([@$&])\\{)(.*?)(})$");
        hasVariablePattern = Pattern.compile("(.*)(([@$&])\\{)(.*?)(})(.*)");
        findVariablePattern = Pattern.compile("(([@$&])\\{)(.*?)(})");
        escapePattern = Pattern.compile("[[<>!={}()\\[\\].+*?^$\\\\|-]]");
    }

    private Node parent;
    private Token value;
    private Pattern match;
    private Map<Token, Set<Variable>> variables;

    public Value(Node parent, Token value) {
        this.parent = parent;
        this.value = value;
        this.variables = new HashMap<>();

        buildRegex();
    }

    public Value(Token value){
        this(null, value);
    }

    public void setVariable(Token name, Variable variable) throws InvalidDependencyException {
        if(variable == null){
            return;
        }

        this.variables.putIfAbsent(name, new HashSet<>());
        Set<Variable> variables = this.variables.get(name);
        variables.add(variable);

        if(parent != null){
            variable.addDependency(parent);
        }
    }

    public void setVariable(Token name, Set<Variable> variables) throws InvalidDependencyException {
        for(Variable variable: variables){
            setVariable(name, variable);
        }
    }

    @Override
    public String toString() {
        return this.value.getValue();
    }

    @Override
    public Token getName(){
        return this.value;
    }

    public boolean matches(Token token) {
        Matcher matcher = match.matcher(token.getValue());
        return matcher.matches();
    }

    public boolean isVariable() {
        Matcher matcher = getVariableMatcher(this.value, Matching.IS_VARIABLE);
        return matcher.matches();
    }

    public static boolean isVariable(Token token) {
        Value value = new Value(token);
        return value.isVariable();
    }

    public boolean hasVariable() {
        Matcher matcher = getVariableMatcher(this.value, Matching.HAS_VARIABLE);
        return matcher.matches();
    }

    public static boolean hasVariable(Token token) {
        Value value = new Value(token);
        return value.hasVariable();
    }

    public List<Token> findVariables() {
        List<Token> variables = new ArrayList<>();

        Matcher matcher = getVariableMatcher(this.value, Matching.FIND_VARIABLE);

        while (matcher.find()){
            variables.add(this.value.extract(matcher.start(), matcher.end()));
        }

        return variables;
    }

    public static List<Token> findVariables(Token token) {
        Value value = new Value(token);
        return value.findVariables();
    }

    private void buildRegex() {
        Matcher matcher = getVariableMatcher(this.value, Matching.FIND_VARIABLE);

        String placeholder = "@@@@___VARIABLE__PLACEHOLDER___@@@@";

        String pattern = matcher.replaceAll(placeholder).trim();
        pattern = escape(pattern);
        pattern = pattern.replaceAll(placeholder, "(.*)");

        match = Pattern.compile("^" + pattern + "$", Pattern.CASE_INSENSITIVE);
    }

    private static Matcher getVariableMatcher(Token token, Matching matching) {
        Matcher matcher;
        String name = token.getValue();

        switch (matching) {
            case IS_VARIABLE:
                matcher = isVariablePattern.matcher(name);
                break;
            case HAS_VARIABLE:
                matcher = hasVariablePattern.matcher(name);
                break;
            case FIND_VARIABLE:
                matcher = findVariablePattern.matcher(name);
                break;

            default:
                throw new InvalidParameterException(String.format("Matcher of type %s not supported", matching.name()));
        }

        return matcher;
    }

    public static String escape(String s){
        return escapePattern.matcher(s).replaceAll("\\\\$0");
    }

    public static String getBareVariableName(String s){
        return s.replaceAll("(^[&@$]\\{)|(}$)", "").trim();
    }

    public static String getGenericVariableName(String s){
        return s.replaceAll("[\\s_]", "").toLowerCase().trim();
    }

    @Override
    public double distance(Differentiable other) {
        if(other == null){
            return 1.0;
        }

        if(other == this){
            return 0.0;
        }

        if(!(other instanceof Value)){
            return 1.0;
        }

        Value value = (Value)other;
        return this.value.equals(value.value) ? 0 : 1;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        if(other == this){
            return Collections.emptyList();
        }

        if(!Value.class.isAssignableFrom(other.getClass())){
            return Collections.singletonList(Action.addElement(Value.class, this));
        }

        Value value = (Value)other;

        if(value.getName().equalsValue(this.getName())){
            return Collections.emptyList();
        }

        return Collections.singletonList(Action.addElement(Value.class, this));
    }

    public static Value empty(){
        return new Value(Token.empty());
    }
}
