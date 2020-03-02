package tech.ikora.builder;

import org.apache.commons.math3.util.Pair;
import tech.ikora.model.Token;
import tech.ikora.model.Tokens;
import tech.ikora.utils.StringUtils;

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

    static Pair<Token, Token> getKeyValuePair(Token token){
        int current = 0;
        int breakPosition = 0;
        boolean escape = false;

        for(char c: token.getText().toCharArray()){
            if(!escape && c == '='){
                break;
            }

            if(c == '\\'){
                breakPosition = ++current;
                escape = true;
                continue;
            }

            breakPosition = ++current;
            escape = false;
        }

        return new Pair<>(token.extract(0, breakPosition), token.extract(Math.min(token.length(), ++breakPosition)));
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
        final State state = new State();

        for(char c: text.toCharArray()){
            if(state.comment) {
                ++state.current;
                continue;
            }

            switch (c){
                case '#':
                    if (!state.escape){
                        tokens.add(createToken(line.getNumber(), state.start, text.substring(state.start, state.current)));
                        state.reset();
                        state.comment = true;
                    }
                    break;
                case ' ':
                    ++state.spaces;
                    break;
                case '\t':
                    ++state.tabs;
                    break;
                case '\n':
                case '\r':
                    tokens.add(createToken(line.getNumber(), state.start, text.substring(state.start, state.current)));
                    state.reset();
                    break;
                case '\\':
                    state.escape = true;
                default:
                    if(state.isNewToken()){
                        tokens.add(createToken(line.getNumber(), state.start, text.substring(state.start, state.current)));
                        state.start =  state.current;
                    }

                    state.spaces = state.tabs = 0;
            }

            ++state.current;
        }

        if(state.current > state.start) tokens.add(createToken(line.getNumber(), state.start, text.substring(state.start, state.current)));

        return tokens;
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

    private static class State{
        int start = 0;
        int current = 0;
        int spaces = 0;
        int tabs = 0;
        boolean escape = false;
        boolean comment = false;

        void reset(){
            spaces = 0;
            tabs = 0;
            start = current;
        }

        boolean isNewToken(){
            return spaces > 1 || tabs > 0;
        }
    }
}
