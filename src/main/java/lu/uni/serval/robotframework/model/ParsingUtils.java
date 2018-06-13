package lu.uni.serval.robotframework.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingUtils {
    private ParsingUtils(){}

    static String[] tokenizeLine(String line){
        return line.split("\t");
    }

    static boolean compareNoCase(String line, String regex){
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);

        return matcher.matches();
    }
}
