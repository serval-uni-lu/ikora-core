package org.ukwikora.report;

import org.ukwikora.utils.EasyPair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutputMessageParser {
    private static final Set<EasyPair<String, String>> ignoreSet;
    private static final Map<EasyPair<String, String>, EasyPair<String, Integer>> successDictionary;
    private static final Map<EasyPair<String, String>, EasyPair<String, Integer>> errorDictionary;

    static {
        ignoreSet = initializeIgnoreSet();
        successDictionary = initializeSuccessDictionary();
        errorDictionary = initializeErrorDictionary();
    }

    public static List<String> parseArguments(String message, String keyword, String library, Status status){
        if(ignoreSet.contains(new EasyPair<>(library, keyword))){
            return new ArrayList<>();
        }

        EasyPair<String, Integer> format = getFormat(keyword, library, status);

        if (format == null){
            System.out.println("failed to parse message [" + library + ":" + keyword + "][" + status.toString() + "]: " + message);
            return new ArrayList<>();
        }

        if(format.right == 0){
            return new ArrayList<>();
        }

        return extractArguments(message, format.left, format.right);
    }

    private static EasyPair<String, Integer> getFormat(String keyword, String library, Status status){
        if(status.getType() == Status.Type.FAILED){
            return errorDictionary.get(new EasyPair<>(library.toLowerCase(), keyword.toLowerCase()));
        }

        return successDictionary.get(new EasyPair<>(library.toLowerCase(), keyword.toLowerCase()));
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
        String variables = "(\\{[0-9]+})";

        Pattern pattern = Pattern.compile(variables);
        Matcher matcher = pattern.matcher(format);

        StringBuilder builder = new StringBuilder();

        int end = 0;
        while (matcher.find()){
            if(matcher.start() > end){
                builder.append("(");
                builder.append(format, end, matcher.start());
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
            builder.append(format, end, format.length());
            builder.append(")");
            argumentPositions.add(-1);
        }

        return builder.toString();
    }

    private static Set<EasyPair<String,String>> initializeIgnoreSet() {
        HashSet<EasyPair<String, String>> set = new HashSet<>();

        set.add(new EasyPair<>("AutoItLibrary", "Win Get Title"));
        set.add(new EasyPair<>("AutoItLibrary", "Win Wait"));

        set.add(new EasyPair<>("BuiltIn", "Log"));
        set.add(new EasyPair<>("BuiltIn", "Run Keyword And Return Status"));
        set.add(new EasyPair<>("BuiltIn", "Get Regex Matches"));
        set.add(new EasyPair<>("BuiltIn", "Evaluate"));
        set.add(new EasyPair<>("BuiltIn", "Repeat Keyword"));
        set.add(new EasyPair<>("BuiltIn", "Get Count"));

        set.add(new EasyPair<>("Collections", "Get From List"));

        set.add(new EasyPair<>("DateTime", "Get Current Date"));

        set.add(new EasyPair<>("Selenium2Library", "Get Source"));
        set.add(new EasyPair<>("Selenium2Library", "Get List Items"));

        return set;
    }

    private static Map<EasyPair<String,String>, EasyPair<String, Integer>> initializeSuccessDictionary() {
        Map<EasyPair<String, String>, EasyPair<String, Integer>> dictionary = new HashMap<>();

        dictionary.put(new EasyPair<>("builtin", "sleep"), new EasyPair<>("Slept {0} seconds?", 1));
        dictionary.put(new EasyPair<>("builtin", "set suite variable"), new EasyPair<>("(.*) = {0}", 1));
        dictionary.put(new EasyPair<>("builtin", "set variable"), new EasyPair<>("(.*) = {0}", 1));

        dictionary.put(new EasyPair<>("datetime", "convert date"), new EasyPair<>("(.*) = {0}", 1));

        dictionary.put(new EasyPair<>("selenium2library", "element should not contain"), new EasyPair<>("DifferentiableString '{0}' contains text '{1}'.", 2));
        dictionary.put(new EasyPair<>("selenium2library", "element should contain"), new EasyPair<>("DifferentiableString '{0}' does not contains text '{1}'.", 2));
        dictionary.put(new EasyPair<>("selenium2library", "page should contain"), new EasyPair<>("Current page contains text '{0}'.", 1));
        dictionary.put(new EasyPair<>("selenium2library", "page should contain element"), new EasyPair<>("Current page contains '{0}' element(s).", 1));
        dictionary.put(new EasyPair<>("selenium2library", "locator should match x times"), new EasyPair<>("Current page contains %s elements matching '%s'.", 2));
        dictionary.put(new EasyPair<>("selenium2library", "page should not contain"), new EasyPair<>("Current page does not contain text '{0}'.", 1));
        dictionary.put(new EasyPair<>("selenium2library", "capture page screenshot"), new EasyPair<>("</td></tr><tr><td colspan=\"3\"><a href=\"{0}\"><img src=\"{0}\" width=\"800px\"></a>", 0));
        dictionary.put(new EasyPair<>("selenium2library", "open browser"), new EasyPair<>("Opening browser '{1}' to base url '{0}'.", 2));
        dictionary.put(new EasyPair<>("selenium2library", "click element"), new EasyPair<>("Clicking element '{0}'.", 1));
        dictionary.put(new EasyPair<>("selenium2library", "click button"), new EasyPair<>("Clicking button '{0}'.", 1));
        dictionary.put(new EasyPair<>("selenium2library", "element should be visible"), new EasyPair<>("DifferentiableString '{0}' is displayed.", 1));
        dictionary.put(new EasyPair<>("selenium2library", "input password"), new EasyPair<>("Typing password into text field '{0}'.", 1));
        dictionary.put(new EasyPair<>("selenium2library", "input text"), new EasyPair<>("Typing text '{1}' into text field '{0}'.", 2));
        dictionary.put(new EasyPair<>("selenium2library", "textfield value should be"), new EasyPair<>("Content of text field '{1}' is '{0}'.", 2));
        dictionary.put(new EasyPair<>("selenium2library", "element should not be visible"), new EasyPair<>("DifferentiableString '{0}' did not exist.", 1));
        dictionary.put(new EasyPair<>("selenium2library", "click link"), new EasyPair<>("Clicking link '{0}'.", 1));
        dictionary.put(new EasyPair<>("selenium2library", "get text"), new EasyPair<>("(.*) = {0}", 1));
        dictionary.put(new EasyPair<>("selenium2library", "textarea value should be"), new EasyPair<>("Content of text area '{0}' is '{1}'.", 2));
        dictionary.put(new EasyPair<>("selenium2library", "get matching xpath count"), new EasyPair<>("(.*) = (.*)", 0));
        dictionary.put(new EasyPair<>("selenium2library", "xpath should match x times"), new EasyPair<>("Current Page contains {1} elements matching '{0}'", 2));

        dictionary.put(new EasyPair<>("string", "get regexp matches"), new EasyPair<>("(.*) = {1}.", 2));
        dictionary.put(new EasyPair<>("string", "strip string"), new EasyPair<>("(.*) = {0}.", 1));

        return dictionary;
    }

    private static Map<EasyPair<String,String>, EasyPair<String, Integer>> initializeErrorDictionary() {
        Map<EasyPair<String, String>, EasyPair<String, Integer>> dictionary = new HashMap<>();

        dictionary.put(new EasyPair<>("builtin", "wait until keyword succeeds"), new EasyPair<>("Keyword '{0}' failed after retrying for 5 seconds.%", 1));
        dictionary.put(new EasyPair<>("builtin", "should be equal"), new EasyPair<>("{0} != {1}", 2));

        dictionary.put(new EasyPair<>("selenium2library", "wait until page contains"), new EasyPair<>("Text '{0}' did not appear in 2 seconds", 1));
        dictionary.put(new EasyPair<>("selenium2library", "click element"), new EasyPair<>("DifferentiableString with locator '{0}' not found.", 1));
        dictionary.put(new EasyPair<>("selenium2library", "wait until element is visible"), new EasyPair<>("DifferentiableString '{0}' not visible after 5 seconds.", 1));
        dictionary.put(new EasyPair<>("selenium2library", "click button"), new EasyPair<>("Button with locator '{0}' not found.", 1));
        dictionary.put(new EasyPair<>("selenium2library", "element should not be visible"), new EasyPair<>("The element '{0}' should not be visible, but it is.", 1));
        dictionary.put(new EasyPair<>("selenium2library", "textarea value should be"), new EasyPair<>("The area '{0}' should have had text '{1}' but it had '(.*)'.", 1));
        dictionary.put(new EasyPair<>("selenium2library", "input text"), new EasyPair<>("", 0));
        dictionary.put(new EasyPair<>("selenium2library", "wait until page contains element"), new EasyPair<>("DifferentiableString '{0}' did not appear in 30 seconds.", 1));
        dictionary.put(new EasyPair<>("selenium2library", "open browser"), new EasyPair<>("WebDriverException: Message: Reached error page: (.*)", 0));
        dictionary.put(new EasyPair<>("selenium2library", "element should be visible"), new EasyPair<>("The element '{0}' should be visible, but it is not.", 1));
        dictionary.put(new EasyPair<>("selenium2library", "page should contain"), new EasyPair<>("Page should have contained text '{0}' but did not", 1));
        dictionary.put(new EasyPair<>("selenium2library", "wait until element is enabled"), new EasyPair<>("DifferentiableString locator '{0}' did not match any elements after 30 seconds", 1));

        return dictionary;
    }
}
