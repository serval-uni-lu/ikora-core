package org.ikora.builder;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.ikora.model.Token;

import java.util.*;
import java.util.stream.Collectors;

public class Tokens implements Iterable<Token> {
    private final SortedSet<Token> tokenList;

    public Tokens(){
        this.tokenList = new TreeSet<>();
    }

    private Tokens(List<Token> tokenList){
        this.tokenList = new TreeSet<>(tokenList);
    }

    public void add(Token token){
        if((token.isDelimiter() && containsDelimiterOnly())
            || !token.isDelimiter()){
            this.tokenList.add(token);
        }
    }

    public void addAll(Tokens tokens) {
        for(Token token: tokens){
            add(token);
        }
    }

    public Tokens setType(Token.Type type){
        this.tokenList.forEach(token -> token.setType(type));
        return this;
    }

    public Token get(int position){
        Iterator<Token> iterator = offset(position);
        return iterator.next();
    }

    public Token first(){
        return this.tokenList.first();
    }

    public Token last() {
        return this.tokenList.last();
    }

    public int size(){
        return this.tokenList.size();
    }

    public Tokens withoutIndent(){
        return new Tokens(tokenList.stream().filter(token -> !token.isDelimiter()).collect(Collectors.toList()));
    }

    public Tokens withoutFirst(){
        return withoutFirst(0);
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

        if(LexerUtils.compareNoCase(first().getText(), tag)){
            return withoutFirst();
        }

        return this;
    }

    public int getIndentSize() {
        return (int) tokenList.stream().filter(Token::isDelimiter).count();
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
        return this.tokenList.iterator();
    }

    @Override
    public String toString() {
        return this.tokenList.stream()
                .map(Token::getText)
                .collect(Collectors.joining("\t"));
    }

    public boolean isEmpty() {
        return this.tokenList.isEmpty();
    }

    private boolean containsDelimiterOnly(){
        return tokenList.stream().allMatch(Token::isDelimiter);
    }

    private Iterator<Token> offset(int offset){
        if(offset < 0 || offset >= size()){
            throw new OutOfRangeException(offset, 0, size() - 1);
        }

        int i = 0;
        Iterator<Token> iterator = this.tokenList.iterator();

        while (i++ < offset) iterator.next();

        return iterator;
    }
}
