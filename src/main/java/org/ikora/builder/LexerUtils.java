package org.ikora.builder;

import org.ikora.model.Token;
import org.ikora.model.Tokens;
import org.ikora.utils.StringUtils;

import java.io.IOException;

class LexerUtils {
    private LexerUtils(){}

    static Tokens parseMultiLine(LineReader reader, Tokens tokens, StringBuilder builder) throws IOException {
        Tokens collect = new Tokens();
        collect.addAll(tokens.setType(Token.Type.DOCUMENTATION));

        builder.append(tokens.toString());
        appendMultiline(reader, builder, collect);

        return collect;
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
        return StringUtils.compareNoCase(value, regex);
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

    private static void appendMultiline(LineReader reader, StringBuilder result, Tokens collect) throws IOException {
        while(reader.readLine().isValid()){
            Line line = reader.getCurrent();

            if(ignore(line.getText())){
                continue;
            }

            Tokens tokens = tokenize(line).withoutIndent();

            if(tokens.isEmpty()){
                continue;
            }

            if(!StringUtils.compareNoCase(tokens.first().getText(), "\\.\\.\\.")){
                break;
            }

            result.append("\n");
            result.append(tokens.withoutFirst().toString());
            collect.add(tokens.first().setType(Token.Type.CONTINUATION));
            collect.addAll(tokens.withoutFirst().setType(Token.Type.DOCUMENTATION));
        }
    }

    private static Token createToken(int line, int offset, String text){
        String trimmed = text.trim();

        Token.Type type;
        String value;

        if(trimmed.isEmpty() || trimmed.equals("\\")){
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
            else if(StringUtils.compareNoCase(value,"^:(\\s?)FOR")){
                type = Token.Type.FOR_LOOP;
            }
            else if(StringUtils.compareNoCase(value, "^((\\$|@|&)\\{)(.*)(\\})(\\s?)(=?)")){
                type = Token.Type.ASSIGNMENT;
            }
            else{
                type = Token.Type.TEXT;
            }
        }

        return new Token(value, line, offset, offset + value.length(), type);
    }
}
