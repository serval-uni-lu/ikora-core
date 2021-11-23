package lu.uni.serval.ikora.core.utils;

import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Tokens;

import java.util.Iterator;
import java.util.regex.Pattern;

public class TokenUtils {
    public static final Pattern equalsFinder = Pattern.compile("(\\s*=\\s*)$");

    private TokenUtils() {}

    public static Tokens accumulate(Iterator<Token> tokenIterator){
        final Tokens tokens = new Tokens();
        while (tokenIterator != null && tokenIterator.hasNext()) tokens.add(tokenIterator.next());

        return tokens;
    }

    public static Token trimRightEquals(Token token) {
        return token.trim(equalsFinder);
    }

    public static Token extractEqualSign(Token token) {
        return token.extract(equalsFinder);
    }
}
