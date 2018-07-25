package lu.uni.serval.robotframework.compiler;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Utils {
    private Utils(){}

    static boolean compareNoCase(String line, String regex){
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line.trim());

        return matcher.matches();
    }

    static boolean isBlock(String line, String block){
        String regex = String.format("^\\*\\*\\*(\\s*)%s(\\s*)\\*\\*\\*", block);
        return Utils.compareNoCase(line, regex);
    }

    static boolean isComment(String line){
        return line.trim().startsWith("#");
    }

    static boolean ignore(Line line){
        return line.isEmpty() || isComment(line.getText());
    }

    static boolean isBlock(String line) {
        return isBlock(line, "(.+)");
    }

    static String[] removeIndent(String[] tokens){
        while (tokens[0].isEmpty()){
            tokens = Arrays.copyOfRange(tokens, 1, tokens.length);
        }

        return tokens;
    }

    static void parseDocumentation(LineReader reader, String[] tokens, StringBuilder builder) throws IOException {
        tokens = Utils.removeIndent(tokens);

        if(tokens.length > 1){
            builder.append(Utils.removeIndent(tokens)[1]);
        }
        appendMultiline(reader, builder);
    }

    static void appendMultiline(LineReader reader, StringBuilder result) throws IOException {
        Line line;

        while((line = reader.readLine()) != null){
            if(Utils.ignore(line)){
                continue;
            }

            String[] tokens = Utils.removeIndent(line.tokenize());

            if(!tokens[0].startsWith("...")){
                break;
            }

            result.append("\n");

            if(tokens.length > 1) {
                result.append(tokens[1]);
            }
        }
    }
}
