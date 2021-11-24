package lu.uni.serval.ikora.core.model;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.exception.OutOfRangeException;

import java.util.*;
import java.util.stream.Collectors;

public class Tokens implements Iterable<Token> {
    private final SortedSet<Token> tokenSet;

    public Tokens(){
        this.tokenSet = new TreeSet<>();
    }

    public Tokens(Token... tokens){
        this(Arrays.asList(tokens));
    }

    private Tokens(List<Token> tokenSet){
        this.tokenSet = new TreeSet<>(tokenSet);
    }

    public static Tokens empty() {
        return new Tokens();
    }

    public SortedSet<Token> asSortedSet(){
        return this.tokenSet;
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
        this.tokenSet.stream()
                .filter(Token::isText)
                .forEach(token -> token.setType(type));
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
                .map(Token::toString)
                .collect(Collectors.joining(" "));
    }

    public boolean equalsIgnorePosition(Tokens other){
        if(this == other){
            return true;
        }

        if(other == null || this.size() != other.size()){
            return false;
        }

        for(int i = 0; i < size(); ++i){
            if (!this.get(i).matches(other.get(i))){
                return false;
            }
        }

        return true;
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

    public int getLoc() {
        return (int)this.tokenSet.stream()
                .map(Token::getLine)
                .distinct()
                .count();
    }

    public Tokens clean() {
        return new Tokens(tokenSet.stream()
                .filter(token -> !token.isComment() && !token.isContinuation() && !token.isDelimiter() && !token.isEmpty())
                .collect(Collectors.toList()));
    }
}
