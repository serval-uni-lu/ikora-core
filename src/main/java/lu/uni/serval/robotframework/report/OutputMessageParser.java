package lu.uni.serval.robotframework.report;

import lu.uni.serval.utils.ReportKeywordData;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutputMessageParser {
    final static private Set<ImmutablePair<String, String>> ignoreSet;
    final static private Map<ImmutablePair<String, String>, ImmutablePair<String, Integer>> successDictionary;
    final static private Map<ImmutablePair<String, String>, ImmutablePair<String, Integer>> errorDictionary;

    static {
        ignoreSet = initializeIgnoreSet();
        successDictionary = initializeSuccessDictionary();
        errorDictionary = initializeErrorDictionary();
    }

    public static List<String> parseArguments(String message, String keyword, String library, ReportKeywordData.Status status){
        if(ignoreSet.contains(new ImmutablePair<>(library, keyword))){
            return new ArrayList<>();
        }

        ImmutablePair<String, Integer> format = getFormat(keyword, library, status);

        if (format == null){
            System.out.println("failed to parse message [" + library + ":" + keyword + "][" + status.toString() + "]: " + message);
            return new ArrayList<>();
        }

        if(format.right == 0){
            return new ArrayList<>();
        }

        return extractArguments(message, format.left, format.right);
    }

    private static ImmutablePair<String, Integer> getFormat(String keyword, String library, ReportKeywordData.Status status){
        if(status == ReportKeywordData.Status.FAILED){
            return errorDictionary.get(new ImmutablePair<>(library.toLowerCase(), keyword.toLowerCase()));
        }

        return successDictionary.get(new ImmutablePair<>(library.toLowerCase(), keyword.toLowerCase()));
    }

    private static List<String> extractArguments(String message, String format, int numberArguments){
        List<String> arguments = new ArrayList<>(Collections.nCopies(numberArguments, ""));
        List<Integer> argumentPositions = new ArrayList<>();

        String regex = createRegex(format, argumentPositions);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        if(matcher.find()){
            int groupCount = matcher.groupCount();
            for(int groupIndex = 1; groupIndex <groupCount; ++groupIndex){
                String token = matcher.group(groupIndex);
                int position = argumentPositions.get(groupIndex - 1);
                if(position != -1){
                    arguments.set(position, token);
                }
            }
        }

        return arguments;
    }

    private static String createRegex(String format, List<Integer> argumentPositions) {
        String variables = "(\\{[0-9]+\\})";

        Pattern pattern = Pattern.compile(variables);
        Matcher matcher = pattern.matcher(format);

        StringBuilder builder = new StringBuilder();

        int end = 0;
        while (matcher.find()){
            if(matcher.start() > end){
                builder.append("(");
                builder.append(format.substring(end, matcher.start()));
                builder.append(")");
                argumentPositions.add(-1);
            }
            builder.append("(.*)");
            String position = format.substring(matcher.start() + 1, matcher.end() - 1);
            argumentPositions.add(Integer.valueOf(position));

            end = matcher.end();
        }

        if(format.length() > end){
            builder.append("(");
            builder.append(format.substring(end, format.length()));
            builder.append(")");
            argumentPositions.add(-1);
        }

        return builder.toString();
    }

    private static Set<ImmutablePair<String,String>> initializeIgnoreSet() {
        HashSet<ImmutablePair<String, String>> set = new HashSet<>();

        set.add(new ImmutablePair<>("AutoItLibrary", "Win Get Title"));
        set.add(new ImmutablePair<>("AutoItLibrary", "Win Wait"));

        set.add(new ImmutablePair<>("BuiltIn", "Log"));
        set.add(new ImmutablePair<>("BuiltIn", "Run Keyword And Return Status"));
        set.add(new ImmutablePair<>("BuiltIn", "Get Regex Matches"));
        set.add(new ImmutablePair<>("BuiltIn", "Evaluate"));
        set.add(new ImmutablePair<>("BuiltIn", "Repeat Keyword"));
        set.add(new ImmutablePair<>("BuiltIn", "Get Count"));

        set.add(new ImmutablePair<>("Collections", "Get From List"));

        set.add(new ImmutablePair<>("DateTime", "Get Current Date"));

        set.add(new ImmutablePair<>("Selenium2Library", "Get Source"));
        set.add(new ImmutablePair<>("Selenium2Library", "Get List Items"));

        return set;
    }

    private static Map<ImmutablePair<String,String>, ImmutablePair<String, Integer>> initializeSuccessDictionary() {
        Map<ImmutablePair<String, String>, ImmutablePair<String, Integer>> dictionary = new HashMap<>();

        dictionary.put(new ImmutablePair<>("builtin", "sleep"), new ImmutablePair<>("Slept {0} seconds?", 1));
        dictionary.put(new ImmutablePair<>("builtin", "set suite variable"), new ImmutablePair<>("(.*) = {0}", 1));
        dictionary.put(new ImmutablePair<>("builtin", "set variable"), new ImmutablePair<>("(.*) = {0}", 1));

        dictionary.put(new ImmutablePair<>("datetime", "convert date"), new ImmutablePair<>("(.*) = {0}", 1));

        dictionary.put(new ImmutablePair<>("selenium2library", "element should not contain"), new ImmutablePair<>("Element '{0}' contains text '{1}'.", 2));
        dictionary.put(new ImmutablePair<>("selenium2library", "element should contain"), new ImmutablePair<>("Element '{0}' does not contains text '{1}'.", 2));
        dictionary.put(new ImmutablePair<>("selenium2library", "page should contain"), new ImmutablePair<>("Current page contains text '{0}'.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "page should contain element"), new ImmutablePair<>("Current page contains '{0}' element(s).", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "locator should match x times"), new ImmutablePair<>("Current page contains %s elements matching '%s'.", 2));
        dictionary.put(new ImmutablePair<>("selenium2library", "page should not contain"), new ImmutablePair<>("Current page does not contain text '{0}'.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "capture page screenshot"), new ImmutablePair<>("</td></tr><tr><td colspan=\"3\"><a href=\"{0}\"><img src=\"{0}\" width=\"800px\"></a>", 0));
        dictionary.put(new ImmutablePair<>("selenium2library", "open browser"), new ImmutablePair<>("Opening browser '{1}' to base url '{0}'.", 2));
        dictionary.put(new ImmutablePair<>("selenium2library", "click element"), new ImmutablePair<>("Clicking element '{0}'.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "click button"), new ImmutablePair<>("Clicking button '{0}'.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "element should be visible"), new ImmutablePair<>("Element '{0}' is displayed.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "input password"), new ImmutablePair<>("Typing password into text field '{0}'.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "input text"), new ImmutablePair<>("Typing text '{1}' into text field '{0}'.", 2));
        dictionary.put(new ImmutablePair<>("selenium2library", "textfield value should be"), new ImmutablePair<>("Content of text field '{1}' is '{0}'.", 2));
        dictionary.put(new ImmutablePair<>("selenium2library", "element should not be visible"), new ImmutablePair<>("Element '{0}' did not exist.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "click link"), new ImmutablePair<>("Clicking link '{0}'.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "get text"), new ImmutablePair<>("(.*) = {0}", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "textarea value should be"), new ImmutablePair<>("Content of text area '{0}' is '{1}'.", 2));

        dictionary.put(new ImmutablePair<>("string", "get regexp matches"), new ImmutablePair<>("(.*) = {1}.", 2));
        dictionary.put(new ImmutablePair<>("string", "strip string"), new ImmutablePair<>("(.*) = {0}.", 1));

        return dictionary;
    }

    private static Map<ImmutablePair<String,String>, ImmutablePair<String, Integer>> initializeErrorDictionary() {
        Map<ImmutablePair<String, String>, ImmutablePair<String, Integer>> dictionary = new HashMap<>();

        dictionary.put(new ImmutablePair<>("builtin", "wait until keyword succeeds"), new ImmutablePair<>("Keyword '{0}' failed after retrying for 5 seconds.%", 1));

        dictionary.put(new ImmutablePair<>("selenium2library", "wait until page contains"), new ImmutablePair<>("Text '{0}' did not appear in 2 seconds", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "click element"), new ImmutablePair<>("Element with locator '{0}' not found.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "wait until element is visible"), new ImmutablePair<>("Element '{0}' not visible after 5 seconds.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "click button"), new ImmutablePair<>("Button with locator '{0}' not found.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "element should not be visible"), new ImmutablePair<>("The element '{0}' should not be visible, but it is.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "textarea value should be"), new ImmutablePair<>("The area '{0}' should have had text '{1}' but it had '(.*)'.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "input text"), new ImmutablePair<>("", 0));
        dictionary.put(new ImmutablePair<>("selenium2library", "wait until page contains element"), new ImmutablePair<>("Element '{0}' did not appear in 30 seconds.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "open browser"), new ImmutablePair<>("WebDriverException: Message: Reached error page: (.*)", 0));

        return dictionary;
    }
}
