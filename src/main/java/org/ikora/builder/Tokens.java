package org.ikora.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Tokens implements Iterable<Token> {
    private final List<Token> tokens;

    public Tokens(){
        this.tokens = new ArrayList<>();
    }

    private Tokens(List<Token> tokens){
        this.tokens = tokens;
    }

    public void add(Token token){
        if((token.isDelimiter() && containsDelimiterOnly())
            || !token.isDelimiter()){
            this.tokens.add(token);
        }
    }

    public Optional<Token> first(){
        if(this.size() > 0){
            return Optional.of(this.tokens.get(0));
        }

        return Optional.empty();
    }

    public Optional<Token> last() {
        if(this.size() > 0){
            return Optional.of(this.tokens.get(this.tokens.size() - 1));
        }

        return Optional.empty();
    }

    public int size(){
        return this.tokens.size();
    }

    public Tokens withoutIndent(){
        return new Tokens(tokens.stream().filter(token -> !token.isDelimiter()).collect(Collectors.toList()));
    }

    public Tokens withoutFirst(){
        return withoutFirst(1);
    }

    public Tokens withoutFirst(int offset){
        return new Tokens(this.tokens.subList(offset, tokens.size()));
    }

    public Tokens withoutTag(String tag){
        Optional<Token> first = first();

        if(tag.isEmpty() || !first.isPresent()){
            return this;
        }

        if(LexerUtils.compareNoCase(first.get().getValue(), tag)){
            return withoutFirst();
        }

        return this;
    }

    private boolean containsDelimiterOnly(){
        return tokens.stream().allMatch(Token::isDelimiter);
    }

    public int getIndentSize() {
        return (int) tokens.stream().filter(Token::isDelimiter).count();
    }

    public boolean isParent(Tokens tokens) {
        if(tokens.containsDelimiterOnly()){
            return true;
        }

        if(tokens.size() > 0 && tokens.tokens.get(0).isComment()){
            return true;
        }

        return getIndentSize() + 1 == tokens.getIndentSize();
    }

    @Override
    public Iterator<Token> iterator() {
        return this.tokens.iterator();
    }

    @Override
    public String toString() {
        return this.tokens.stream()
                .map(Token::getValue)
                .collect(Collectors.joining("\t"));
    }

    public boolean isEmpty() {
        return this.tokens.isEmpty();
    }
}
