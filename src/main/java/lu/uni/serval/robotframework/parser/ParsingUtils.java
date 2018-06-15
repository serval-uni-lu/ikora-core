package lu.uni.serval.robotframework.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingUtils {
    private ParsingUtils(){}

    static String[] tokenizeLine(String line){
        line = line.replaceAll("\\s\\s(\\s*)", "\t");

        return line.split("\t");
    }

    static boolean compareNoCase(String line, String regex){
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);

        return matcher.matches();
    }

    static public boolean isBlock(String line, String block){
        String regex = String.format("^\\*\\*\\*(\\s*)%s(\\s*)\\*\\*\\*", block);
        return ParsingUtils.compareNoCase(line, regex);
    }

    static public boolean isBlock(String line) {
        return isBlock(line, "(.+)");
    }

    static public String appendMultiline(BufferedReader bufferedReader, StringBuilder result) throws IOException {
        String line;

        while((line = bufferedReader.readLine()) != null){
            String[] tokens = tokenizeLine(line);

            if(tokens.length == 0 || !tokens[0].equalsIgnoreCase("...")){
                break;
            }

            result.append("\n");

            if(tokens.length > 1) {
                result.append(tokens[1]);
            }
        }

        return line;
    }

    static public String[] removeIndent(String[] tokens){
        while (tokens[0].equals("")){
            tokens = Arrays.copyOfRange(tokens, 1, tokens.length);
        }

        return tokens;
    }
}
