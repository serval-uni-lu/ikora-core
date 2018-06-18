package lu.uni.serval.robotframework.parser;

import java.io.LineNumberReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingUtils {
    private ParsingUtils(){}

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

    static public String[] removeIndent(String[] tokens){
        while (tokens[0].isEmpty()){
            tokens = Arrays.copyOfRange(tokens, 1, tokens.length);
        }

        return tokens;
    }

    static public Line parseDocumentation(LineNumberReader reader, String[] tokens, StringBuilder builder) throws IOException {
        builder.append(tokens[1]);
        return appendMultiline(reader, builder);
    }

    static private Line appendMultiline(LineNumberReader reader, StringBuilder result) throws IOException {
        Line line;

        while((line = Line.getNextLine(reader)) != null){
            String[] tokens = line.tokenize();

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
}
