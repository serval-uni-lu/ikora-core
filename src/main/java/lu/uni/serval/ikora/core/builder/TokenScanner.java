package lu.uni.serval.ikora.core.builder;

import com.ibm.icu.impl.data.TokenIterator;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Tokens;

import java.util.Iterator;
import java.util.SortedSet;

public class TokenScanner implements Iterable<Token> {
    private final SortedSet<Token> tokenSet;

    private boolean skipIndent = false;
    private int firstSkipped = 0;
    private String tagSkipped = "";

    private TokenScanner(SortedSet<Token> tokens) {
        this.tokenSet = tokens;
    }

    public static TokenScanner from(Tokens tokens){
        return new TokenScanner(tokens.asSortedSet());
    }

    public TokenScanner skipIndent(boolean skipIndent){
        this.skipIndent = skipIndent;
        return this;
    }

    public TokenScanner skipFirst(){
        firstSkipped = 1;
        return this;
    }

    public TokenScanner skipFirst(int numberSkipped){
        firstSkipped = numberSkipped;
        return this;
    }

    public TokenScanner skipTag(String name){
        tagSkipped = name;
        return this;
    }

    @Override
    public Iterator<Token> iterator() {
        return new TokenIterator();
    }

    private class TokenIterator implements Iterator<Token> {
        Iterator<Token> iterator = tokenSet.iterator();
        int position = 0;
        Token next = null;
        boolean cached = false;

        @Override
        public boolean hasNext() {
            return getNext(false) != null;
        }

        @Override
        public Token next() {
            return getNext(true);
        }

        private Token getNext(boolean clean){
            if(cached){
                cached = !clean;
                return next;
            }

            if(!clean) cached = true;

            if(!iterator.hasNext()){
                return null;
            }

            next = iterator.next();


            return next;
        }
    }
}
