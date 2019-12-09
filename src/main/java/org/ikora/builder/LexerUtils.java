package org.ikora.builder;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LexerUtils {
    private LexerUtils(){}

    static void parseDocumentation(LineReader reader, StringBuilder builder) throws IOException {
        Tokens tokens = tokenize(reader.getCurrent().getText());
        tokens = tokens.withoutIndent();

        if(tokens.size() > 1){
            builder.append(tokens.withoutTag("\\[Documentation\\]").toString());
        }

        appendMultiline(reader, builder);
    }

    static Tokens tokenize(String line){
        Tokens tokens = new Tokens();

        int start = 0;
        int current = 0;
        int spaces = 0;
        int tabs = 0;

        for(char c: line.toCharArray()){
            switch (c){
                case ' ':
                    ++spaces;
                    break;
                case '\t':
                    ++tabs;
                    break;
                case '\n':
                case '\r':
                    tokens.add(createToken(line.substring(start, current), start));
                default:
                    if(spaces > 1 || tabs > 0){
                        tokens.add(createToken(line.substring(start, current), start));
                        start = current;
                    }

                    spaces = tabs = 0;
            }

            ++current;
        }

        if(current > start) tokens.add(createToken(line.substring(start, current), start));

        return tokens;
    }

    static boolean isBlock(String value) {
        return isBlock(value, "(.+)");
    }

    static boolean isBlock(String value, String name){
        String regex = String.format("^\\*{3}(\\s*)%s(\\s*)(\\**)(\\s*)", name);
        return compareNoCase(value, regex);
    }

    static boolean compareNoCase(String value, String regex){
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
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

    private static void appendMultiline(LineReader reader, StringBuilder result) throws IOException {
        Line line;

        while((line = reader.readLine()) != null){
            if(ignore(line.getText())){
                continue;
            }

            Tokens tokens = tokenize(line.getText()).withoutIndent();

            Optional<Token> first = tokens.first();

            if(!first.isPresent()){
                continue;
            }

            if(!compareNoCase(first.get().getValue(), "...")){
                break;
            }

            result.append("\n");
            result.append(tokens.withoutFirst().toString());
        }
    }

    private static Token createToken(String text, int start){
        String trimmed = text.trim();

        Token.Type type;
        String value;

        if(trimmed.isEmpty() || trimmed.equals("/")){
            value = text;
            type = Token.Type.Delimiter;
        }
        else{
            value = trimmed;

            if(isBlock(value)){
                type = Token.Type.Block;
            }
            else if(value.startsWith("#")){
                type = Token.Type.Comment;
            }
            else if(value.equalsIgnoreCase(":FOR")){
                type = Token.Type.ForLoop;
            }
            else if(compareNoCase(value, "^((\\$|@|&)\\{)(.*)(\\})(\\s?)(=?)")){
                type = Token.Type.Assignment;
            }
            else{
                type = Token.Type.Text;
            }
        }

        return new Token(value, start, start + value.length(), type);
    }
}
