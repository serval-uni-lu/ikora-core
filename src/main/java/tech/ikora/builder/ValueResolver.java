package tech.ikora.builder;

import tech.ikora.model.*;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValueResolver {
    private enum Matching{
        IS_VARIABLE, HAS_VARIABLE, FIND_VARIABLE
    }

    private static final Pattern isVariablePattern;
    private static final Pattern hasVariablePattern;
    private static final Pattern findVariablePattern;
    private static final Pattern escapePattern;

    static {
        isVariablePattern = Pattern.compile("^(([@$&])\\{)(.*?)(})$");
        hasVariablePattern = Pattern.compile("(.*)(([@$&])\\{)(.*?)(})(.*)");
        findVariablePattern = Pattern.compile("(([@$&])\\{)(.*?)(})");
        escapePattern = Pattern.compile("[[<>!={}()\\[\\].+*?^$\\\\|-]]");
    }

    private ValueResolver() {}

    public static boolean matches(Token left, Token right){
        return matches(left.getText(), right.getText());
    }

    public static boolean matches(String left, String right) {
        Pattern match = buildRegex(left);
        Matcher matcher = match.matcher(right);
        return matcher.matches();
    }

    public static boolean isVariable(Token token) {
        Matcher matcher = getVariableMatcher(token.getText(), Matching.IS_VARIABLE);
        return matcher.matches();
    }

    public static boolean hasVariable(Token token) {
        Matcher matcher = getVariableMatcher(token.getText(), Matching.HAS_VARIABLE);
        return matcher.matches();
    }

    public static List<Token> findVariables(Token token) {
        List<Token> variables = new ArrayList<>();

        Matcher matcher = getVariableMatcher(token.getText(), Matching.FIND_VARIABLE);

        while (matcher.find()){
            variables.add(token.extract(matcher.start(), matcher.end()));
        }

        return variables;
    }

    private static Pattern buildRegex(String name) {
        Matcher matcher = getVariableMatcher(name, Matching.FIND_VARIABLE);

        String placeholder = "@@@@___VARIABLE__PLACEHOLDER___@@@@";

        String pattern = matcher.replaceAll(placeholder).trim();
        pattern = escape(pattern);
        pattern = pattern.replaceAll(placeholder, "(.*)");

        return Pattern.compile("^" + pattern + "$", Pattern.CASE_INSENSITIVE);
    }

    private static Matcher getVariableMatcher(String name, Matching matching) {
        Matcher matcher;

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

    public static List<Node> getValueNodes(Argument argument){
        final SourceNode definition = argument.getDefinition();

        if(Variable.class.isAssignableFrom(definition.getClass())){
            return getValueNodes((Variable)definition);
        }

        if(Literal.class.isAssignableFrom(definition.getClass())){
            return Collections.singletonList(definition);
        }

        return Collections.emptyList();
    }

    public static List<Node> getValueNodes(Variable variable){
        final Set<Dependable> definition = variable.getDefinition(Link.Import.BOTH);
        final List<Node> values = new ArrayList<>();

        for(Node node: definition){
            if(VariableAssignment.class.isAssignableFrom(node.getClass())){
                values.addAll(((VariableAssignment)node).getValues());
            }
            else if(LibraryVariable.class.isAssignableFrom(node.getClass())){
                values.add(node);
            }
            else if(UserKeyword.class.isAssignableFrom(node.getClass())){
                ((UserKeyword) node).getParameterByName(variable.getNameToken()).ifPresent(values::add);
            }
            else if(Variable.class.isAssignableFrom(node.getClass())){
                values.addAll(getValueNodes((Variable)node));
            }
        }

        return values;
    }

    public static Optional<UserKeyword> getUserKeywordFromArgument(final Variable variable){
        final SourceNode parent = variable.getAstParent();

        if(!(parent instanceof UserKeyword)){
            return Optional.empty();
        }

        UserKeyword userKeyword = (UserKeyword)parent;
        return userKeyword.getArguments().contains(variable) ? Optional.of(userKeyword) : Optional.empty();
    }
}
