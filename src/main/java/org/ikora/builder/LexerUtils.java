package org.ikora.builder;

import org.ikora.model.Token;
import org.ikora.model.Tokens;
import org.ikora.utils.StringUtils;

import java.io.IOException;

class LexerUtils {
    private LexerUtils(){}

    public static Tokens tokenize(LineReader reader) throws IOException {
        Tokens tokens = tokenize(reader.getCurrent());

        while (reader.readLine().isValid()) {
            Line line = reader.getCurrent();

            if (ignore(line.getText())) {
                continue;
            }

            Tokens nextTokens = tokenize(line);

            if (!isContinuation(nextTokens)) {
                break;
            }

            tokens.addAll(nextTokens);
        }

        return tokens;
    }

    public static Tokens peek(Line line) {
        return tokenize(line);
    }

    public static boolean exitBlock(Tokens forLoop, LineReader reader){
        return !forLoop.isParent(LexerUtils.peek(reader.getCurrent())) || LexerUtils.isBlock(reader.getCurrent().getText());
    }

    private static boolean isContinuation(Tokens tokens){
        for(Token token: tokens){
            if(token.isDelimiter()) {
                continue;
            }

            return token.isContinuation();
        }

        return false;
    }

    private static Tokens tokenize(Line line){
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
            else if(StringUtils.compareNoCase(value, "^\\.\\.\\.$")){
                type = Token.Type.CONTINUATION;
            }
            else{
                type = Token.Type.TEXT;
            }
        }

        return new Token(value, line, offset, offset + value.length(), type);
    }
}
