package lu.uni.serval.robotframework.report;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutputMessageParser {
    final static private Map<ImmutablePair<String, String>, ImmutablePair<String, Integer>> successDictionary;

    static {
        successDictionary = initializeSuccessDictionary();
    }

    public static List<String> parseArguments(String message, String keyword, String library){
        ImmutablePair<String, Integer> format = getFormat(keyword, library);

        if (format == null){
            System.out.println("failed to parse message [" + library + ":" + keyword + "]: " + message);
            return new ArrayList<>();
        }

        if(format.right == 0){
            return new ArrayList<>();
        }

        return extractArguments(message, format.left, format.right);
    }

    private static ImmutablePair<String, Integer> getFormat(String keyword, String library){
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

    private static Map<ImmutablePair<String,String>, ImmutablePair<String, Integer>> initializeSuccessDictionary() {
        Map<ImmutablePair<String, String>, ImmutablePair<String, Integer>> dictionary = new HashMap<>();

        dictionary.put(new ImmutablePair<>("selenium2library", "element should not contain"), new ImmutablePair<>("Element '{0}' contains text '{1}'.", 2));
        dictionary.put(new ImmutablePair<>("selenium2library", "element should contain"), new ImmutablePair<>("Element '{0}' does not contains text '{1}'.", 2));
        dictionary.put(new ImmutablePair<>("selenium2library", "page should contain"), new ImmutablePair<>("Current page contains text '{0}'.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "page should contain element"), new ImmutablePair<>("Current page contains '{0}' element(s).", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "locator should match x times"), new ImmutablePair<>("Current page contains %s elements matching '%s'.", 2));

        dictionary.put(new ImmutablePair<>("selenium2library", "capture page screenshot"), new ImmutablePair<>("</td></tr><tr><td colspan=\"3\"><a href=\"{0}\"><img src=\"{0}\" width=\"800px\"></a>", 0));
        dictionary.put(new ImmutablePair<>("selenium2library", "open browser"), new ImmutablePair<>("Opening browser '{1}' to base url '{0}'.", 2));
        dictionary.put(new ImmutablePair<>("selenium2library", "click element"), new ImmutablePair<>("Clicking element '{0}'.", 1));
        dictionary.put(new ImmutablePair<>("selenium2library", "click button"), new ImmutablePair<>("Clicking button '{0}'.", 1));

        return dictionary;
    }
}
