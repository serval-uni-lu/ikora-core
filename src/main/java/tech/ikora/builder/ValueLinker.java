package tech.ikora.builder;

import tech.ikora.model.Token;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValueLinker {
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

    private ValueLinker() {}

    public static boolean matches(Token left, Token right) {
        Pattern match = buildRegex(left);
        Matcher matcher = match.matcher(right.getText());
        return matcher.matches();
    }

    public static boolean isVariable(Token token) {
        Matcher matcher = getVariableMatcher(token, Matching.IS_VARIABLE);
        return matcher.matches();
    }

    public static boolean hasVariable(Token token) {
        Matcher matcher = getVariableMatcher(token, Matching.HAS_VARIABLE);
        return matcher.matches();
    }

    public static List<Token> findVariables(Token token) {
        List<Token> variables = new ArrayList<>();

        Matcher matcher = getVariableMatcher(token, Matching.FIND_VARIABLE);

        while (matcher.find()){
            variables.add(token.extract(matcher.start(), matcher.end()));
        }

        return variables;
    }

    private static Pattern buildRegex(Token token) {
        Matcher matcher = getVariableMatcher(token, Matching.FIND_VARIABLE);

        String placeholder = "@@@@___VARIABLE__PLACEHOLDER___@@@@";

        String pattern = matcher.replaceAll(placeholder).trim();
        pattern = escape(pattern);
        pattern = pattern.replaceAll(placeholder, "(.*)");

        return Pattern.compile("^" + pattern + "$", Pattern.CASE_INSENSITIVE);
    }

    private static Matcher getVariableMatcher(Token token, Matching matching) {
        Matcher matcher;
        String name = token.getText();

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
}
