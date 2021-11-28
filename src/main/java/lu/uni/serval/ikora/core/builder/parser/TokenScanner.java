package lu.uni.serval.ikora.core.builder.parser;

import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Tokens;

import java.util.*;

public class TokenScanner implements Iterable<Token> {
    private final List<Token> tokenSet;

    private boolean skipIndent = false;
    private Set<Token.Type> skippedTypes = new HashSet<>();

    private TokenScanner(List<Token> tokens) {
        this.tokenSet = tokens;
    }

    public static TokenScanner from(Tokens tokens){
        return new TokenScanner(tokens.asList());
    }

    public TokenScanner skipIndent(boolean skipIndent){
        this.skipIndent = skipIndent;
        return this;
    }

    public TokenScanner skipTypes(Token.Type... typesToSkip){
        skippedTypes = new HashSet<>(Arrays.asList(typesToSkip));
        return this;
    }

    @Override
    public Iterator<Token> iterator() {
        return new TokenIterator();
    }

    private class TokenIterator implements Iterator<Token> {
        Iterator<Token> iterator = tokenSet.iterator();
        Token next = null;
        boolean cached = false;

        @Override
        public boolean hasNext() {
            return getNext(false) != null;
        }

        @Override
        public Token next() {
            final Token token = getNext(true);

            if(token == null){
                throw new NoSuchElementException();
            }

            return token;
        }

        private Token getNext(boolean reset){
            if(cached){
                cached = !reset;
                return next;
            }

            if(!reset) cached = true;

            if(!iterator.hasNext()){
                next = null;
            }
            else{
                next = iterator.next();

                while (skipIndent && next != null && next.isDelimiter()){
                    next = iterator.next();
                }

                skipIndent = false;

                while (next != null && skippedTypes.contains(next.getType())) {
                    next = iterator.next();
                }
            }

            return next;
        }
    }
}
