package org.ikora.model;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.ikora.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Tokens implements Iterable<Token> {
    private final SortedSet<Token> tokenSet;

    public Tokens(){
        this.tokenSet = new TreeSet<>();
    }

    private Tokens(List<Token> tokenSet){
        this.tokenSet = new TreeSet<>(tokenSet);
    }

    public void add(Token token){
        if(token == null || token.isEmpty()){
            return;
        }

        if((token.isDelimiter() && containsDelimiterOnly())
            || !token.isDelimiter()){
            this.tokenSet.add(token);
        }
    }

    public void addAll(Tokens tokens) {
        for(Token token: tokens){
            add(token);
        }
    }

    public Tokens setType(Token.Type type){
        this.tokenSet.forEach(token -> token.setType(type));
        return this;
    }

    public Token get(int position){
        Iterator<Token> iterator = offset(position);
        return iterator.next();
    }

    public Token first(){
        if(this.tokenSet.isEmpty()){
            return Token.empty();
        }

        return this.tokenSet.first();
    }

    public Token last() {
        if(this.tokenSet.isEmpty()){
            return Token.empty();
        }

        return this.tokenSet.last();
    }

    public int size(){
        return this.tokenSet.size();
    }

    public Tokens withoutIndent(){
        return new Tokens(tokenSet.stream().filter(token -> !token.isDelimiter()).collect(Collectors.toList()));
    }

    public Tokens withoutFirst(){
        if(tokenSet.size() < 2){
            return new Tokens();
        }

        return withoutFirst(1);
    }

    public Tokens withoutFirst(int offset){
        Iterator<Token> iterator = offset(offset);
        Tokens newTokens = new Tokens();
        while(iterator.hasNext()) newTokens.add(iterator.next());
        return newTokens;
    }

    public Tokens withoutTag(String tag){
        if(tag.isEmpty()){
            return this;
        }

        if(StringUtils.compareNoCase(first().getText(), tag)){
            return withoutFirst();
        }

        return this;
    }

    public int getIndentSize() {
        return (int) tokenSet.stream().filter(Token::isDelimiter).count();
    }

    public boolean isParent(Tokens tokens) {
        if(tokens.containsDelimiterOnly()){
            return true;
        }

        if(tokens.size() > 0 && tokens.first().isComment()){
            return true;
        }

        return getIndentSize() + 1 == tokens.getIndentSize();
    }

    @Override
    public Iterator<Token> iterator() {
        return this.tokenSet.iterator();
    }

    @Override
    public String toString() {
        return this.tokenSet.stream()
                .map(Token::getText)
                .collect(Collectors.joining("\t"));
    }

    public boolean isEmpty() {
        return this.tokenSet.isEmpty();
    }

    private boolean containsDelimiterOnly(){
        return tokenSet.stream().allMatch(Token::isDelimiter);
    }

    private Iterator<Token> offset(int offset){
        if(offset < 0 || offset >= size()){
            throw new OutOfRangeException(offset, 0, size() - 1);
        }

        int i = 0;
        Iterator<Token> iterator = this.tokenSet.iterator();

        while (i++ < offset) iterator.next();

        return iterator;
    }
}
