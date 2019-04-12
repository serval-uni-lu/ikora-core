package org.ukwikora.compiler;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LexerUtils {
    private LexerUtils(){}

    static boolean compareNoCase(String line, String regex){
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line.trim());

        return matcher.matches();
    }

    static String[] removeIndent(String[] tokens){
        while (tokens[0].isEmpty()){
            tokens = Arrays.copyOfRange(tokens, 1, tokens.length);
        }

        return tokens;
    }

    static String[] removeTag(String[] tokens, String tag) {
        if(tag.isEmpty()){
            return tokens;
        }

        if(compareNoCase(tokens[0], tag)){
            tokens = Arrays.copyOfRange(tokens, 1, tokens.length);
        }

        return tokens;
    }

    static void parseDocumentation(LineReader reader, StringBuilder builder) throws IOException {
        String[] tokens = tokenize(reader.getCurrent().getText());
        tokens = LexerUtils.removeIndent(tokens);

        if(tokens.length > 1){
            builder.append(LexerUtils.removeIndent(tokens)[1]);
        }

        appendMultiline(reader, builder);
    }

    static String[] tokenize(String line){
        String tokens = line.replaceAll("\\s+$", "").replaceAll("\\s\\s(\\s*)", "\t");
        tokens = tokens.replaceAll("\\\\t", "\t");

        return tokens.split("\t");
    }

    static boolean isBlock(String line, String block){
        String regex = String.format("^\\*{3}(\\s*)%s(\\s*)(\\**)", block);
        return LexerUtils.compareNoCase(line, regex);
    }

    static boolean isBlock(String line) {
        return isBlock(line, "(.+)");
    }

    static boolean isInBlock(String top, String bottom) {
        if(ignore(bottom)){
            return true;
        }

        return getIndentSize(bottom) == getIndentSize(top) + 1;
    }

    static boolean isEmpty(String line) {
        return line == null || line.trim().isEmpty();
    }

    static boolean isComment(String line){
        return line != null && line.trim().startsWith("#");
    }

    static boolean ignore(String line){
        return isEmpty(line) || isComment(line);
    }

    private static int getIndentSize(String line) {
        String s = line.replaceAll("\\s\\s(\\s*)", "\t");
        s = s.replaceAll("\\\\\\t", "\t");

        int i = 0;

        for (; i < s.length(); i++){
            if(s.charAt(i) != '\t'){
                break;
            }
        }

        return i;
    }

    private static void appendMultiline(LineReader reader, StringBuilder result) throws IOException {
        Line line;

        while((line = reader.readLine()) != null){
            if(ignore(line.getText())){
                continue;
            }

            String[] tokens = LexerUtils.removeIndent(tokenize(line.getText()));

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
