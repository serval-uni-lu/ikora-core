package lu.uni.serval.robotframework.report;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutputMessageParser {
    final static private Map<ImmutablePair<String, String>, String> dictionary;

    static {
        dictionary = initializeDictionary();
    }

    public static List<String> parseArguments(String message, String keyword, String library){
        String format = getFormat(keyword, library);

        if (format == null){
            System.out.println("failed to parse message [" + library + ":" + keyword + "]: " + message);
            return new ArrayList<>();
        }

        return extractArguments(message, format);
    }

    private static String getFormat(String keyword, String library){
        return dictionary.get(new ImmutablePair<>(library.toLowerCase(), keyword.toLowerCase()));
    }

    private static List<String> extractArguments(String message, String format){
        List<String> arguments = new ArrayList<>();
        List<Boolean> isArgumentList = new ArrayList<>();

        String regex = createRegex(format, isArgumentList);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        if(matcher.find()){
            int groupCount = matcher.groupCount();
            for(int groupIndex = 1; groupIndex <groupCount; ++groupIndex){
                String group = matcher.group(groupIndex);
                if(isArgumentList.get(groupIndex - 1)){
                    arguments.add(group);
                }
            }
        }

        return arguments;
    }

    private static String createRegex(String format, List<Boolean> isArgumentList) {
        String variables = "(%s|%d)";

        Pattern pattern = Pattern.compile(variables);
        Matcher matcher = pattern.matcher(format);

        StringBuilder builder = new StringBuilder();

        int end = 0;
        while (matcher.find()){
            if(matcher.start() > end){
                builder.append("(");
                builder.append(format.substring(end, matcher.start()));
                builder.append(")");
                isArgumentList.add(false);
            }
            builder.append("(.*)");
            isArgumentList.add(true);

            end = matcher.end();
        }

        if(format.length() > end){
            builder.append("(");
            builder.append(format.substring(end, format.length()));
            builder.append(")");
            isArgumentList.add(false);
        }

        return builder.toString();
    }

    private static Map<ImmutablePair<String,String>,String> initializeDictionary() {
        Map<ImmutablePair<String, String>, String> dictionary = new HashMap<>();

        dictionary.put(new ImmutablePair<>("selenium2library", "element should not contain"), "Element '%s' contains text '%s'.");
        dictionary.put(new ImmutablePair<>("selenium2library", "element should contain"), "Element '%s' does not contains text '%s'.");
        dictionary.put(new ImmutablePair<>("selenium2library", "page should contain"), "Current page contains text '%s'.");
        dictionary.put(new ImmutablePair<>("selenium2library", "page should contain element"), "Current page contains '%d' element(s).");
        dictionary.put(new ImmutablePair<>("selenium2library", "locator should match x times"), "Current page contains %s elements matching '%s'.");

        dictionary.put(new ImmutablePair<>("selenium2library", "capture page screenshot"), "</td></tr><tr><td colspan=\"3\"><a href=\"%s\"><img src=\"%s\" width=\"800px\"></a>");
        dictionary.put(new ImmutablePair<>("selenium2library", "open browser"), "Opening browser '%s' to base url '%s'.");
        dictionary.put(new ImmutablePair<>("selenium2library", "click element"), "Clicking element '%s'.");
        dictionary.put(new ImmutablePair<>("selenium2library", "click button"), "Clicking button '%s'.");

        return dictionary;
    }
}
