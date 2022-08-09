/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.exception.OutOfRangeException;

import java.util.*;
import java.util.stream.Collectors;

public class Tokens implements Iterable<Token> {
    private final List<Token> tokenList;

    public Tokens(){
        this.tokenList = new ArrayList<>();
    }

    public Tokens(Token... tokens){
        this(Arrays.asList(tokens));
    }

    private Tokens(List<Token> tokenList){
        this.tokenList = tokenList;
    }

    public static Tokens empty() {
        return new Tokens();
    }

    public List<Token> asList(){
        return this.tokenList;
    }

    public void add(Token token){
        if(token == null || token.isEmpty()){
            return;
        }

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
        this.tokenList.stream()
                .filter(Token::isText)
                .forEach(token -> token.setType(type));
        return this;
    }

    public Token get(int position){
        Iterator<Token> iterator = offset(position);
        return iterator.next();
    }

    public Token first(){
        if(this.tokenList.isEmpty()){
            return Token.empty();
        }

        return this.tokenList.get(0);
    }

    public Token last() {
        if(this.tokenList.isEmpty()){
            return Token.empty();
        }

        return this.tokenList.get(this.tokenList.size() - 1);
    }

    public int size(){
        return this.tokenList.size();
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

    public int getLoc() {
        return (int)this.tokenList.stream()
                .map(Token::getLine)
                .distinct()
                .count();
    }

    public Tokens clean() {
        return new Tokens(tokenList.stream()
                .filter(token -> !token.isComment() && !token.isContinuation() && !token.isDelimiter() && !token.isEmpty())
                .collect(Collectors.toList()));
    }
}
