package org.ikora.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Tokens implements Iterable<Token> {
    private final List<Token> tokenList;

    public Tokens(){
        this.tokenList = new ArrayList<>();
    }

    private Tokens(List<Token> tokenList){
        this.tokenList = tokenList;
    }

    public void add(Token token){
        if((token.isDelimiter() && containsDelimiterOnly())
            || !token.isDelimiter()){
            this.tokenList.add(token);
        }
    }

    public Optional<Token> get(int position){
        if(position >= this.size() || position < 0){
            return Optional.empty();
        }

        return Optional.of(this.tokenList.get(position));
    }

    public Optional<Token> first(){
        if(this.size() > 0){
            return Optional.of(this.tokenList.get(0));
        }

        return Optional.empty();
    }

    public Optional<Token> last() {
        if(this.size() > 0){
            return Optional.of(this.tokenList.get(this.tokenList.size() - 1));
        }

        return Optional.empty();
    }

    public int size(){
        return this.tokenList.size();
    }

    public Tokens withoutIndent(){
        return new Tokens(tokenList.stream().filter(token -> !token.isDelimiter()).collect(Collectors.toList()));
    }

    public Tokens withoutFirst(){
        return withoutFirst(1);
    }

    public Tokens withoutFirst(int offset){
        return new Tokens(this.tokenList.subList(offset, tokenList.size()));
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
        return tokenList.stream().allMatch(Token::isDelimiter);
    }

    public int getIndentSize() {
        return (int) tokenList.stream().filter(Token::isDelimiter).count();
    }

    public boolean isParent(Tokens tokens) {
        if(tokens.containsDelimiterOnly()){
            return true;
        }

        if(tokens.size() > 0 && tokens.tokenList.get(0).isComment()){
            return true;
        }

        return getIndentSize() + 1 == tokens.getIndentSize();
    }

    @Override
    public Iterator<Token> iterator() {
        return this.tokenList.iterator();
    }

    @Override
    public String toString() {
        return this.tokenList.stream()
                .map(Token::getValue)
                .collect(Collectors.joining("\t"));
    }

    public boolean isEmpty() {
        return this.tokenList.isEmpty();
    }
}
