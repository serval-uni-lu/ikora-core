package org.ikora.builder;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LexerUtils {
    private LexerUtils(){}

    static void parseDocumentation(LineReader reader, StringBuilder builder) throws IOException {
        Tokens tokens = tokenize(reader.getCurrent());
        tokens = tokens.withoutIndent();

        if(tokens.size() > 1){
            builder.append(tokens.withoutTag("\\[Documentation\\]").toString());
        }

        appendMultiline(reader, builder);
    }

    static Tokens tokenize(Line line){
        Tokens tokens = new Tokens();
        final String text = line.getText();

        int start = 0;
        int current = 0;
        int spaces = 0;
        int tabs = 0;

        for(char c: text.toCharArray()){
            switch (c){
                case ' ':
                    ++spaces;
                    break;
                case '\t':
                    ++tabs;
                    break;
                case '\n':
                case '\r':
                    tokens.add(createToken(line.getNumber(), start, text.substring(start, current)));
                    break;
                default:
                    if(spaces > 1 || tabs > 0){
                        tokens.add(createToken(line.getNumber(), start, text.substring(start, current)));
                        start = current;
                    }

                    spaces = tabs = 0;
            }

            ++current;
        }

        if(current > start) tokens.add(createToken(line.getNumber(), start, text.substring(start, current)));

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

            Tokens tokens = tokenize(line).withoutIndent();

            Optional<Token> first = tokens.first();

            if(!first.isPresent()){
                continue;
            }

            if(!compareNoCase(first.get().getValue(), "\\.\\.\\.")){
                break;
            }

            result.append("\n");
            result.append(tokens.withoutFirst().toString());
        }
    }

    private static Token createToken(int line, int offset, String text){
        String trimmed = text.trim();

        Token.Type type;
        String value;

        if(trimmed.isEmpty() || trimmed.equals("/")){
            value = text;
            type = Token.Type.DELIMITER;
        }
        else{
            value = trimmed;

            if(isBlock(value)){
                type = Token.Type.BLOCK;
            }
            else if(value.startsWith("#")){
                type = Token.Type.COMMENT;
            }
            else if(value.equalsIgnoreCase(":FOR")){
                type = Token.Type.FOR_LOOP;
            }
            else if(compareNoCase(value, "^((\\$|@|&)\\{)(.*)(\\})(\\s?)(=?)")){
                type = Token.Type.ASSIGNMENT;
            }
            else{
                type = Token.Type.TEXT;
            }
        }

        return new Token(value, line, offset, offset + value.length(), type);
    }
}
