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

import lu.uni.serval.ikora.core.exception.InvalidArgumentException;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token implements Comparable<Token> {
    public enum Type{
        TEXT, DELIMITER, BLOCK, COMMENT, ASSIGNMENT, FOR_LOOP, EQUALS, KEYWORD, EMPTY, LABEL, DOCUMENTATION, TAG,
        CONTINUATION, VARIABLE
    }

    private Type type;

    private final int line;
    private final int startOffset;
    private final int endOffset;

    private final String text;

    public Token(String text, int line, int startOffset, int endOffset, Type type){
        this.text = text;
        this.line = line;
        this.startOffset = startOffset;
        this.endOffset = endOffset;

        this.type = type;
    }

    public Token copy(){
        return new Token(this.text, this.line, this.startOffset, this.endOffset, this.type);
    }

    public Token setType(Type type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return type == Type.CONTINUATION ? "\n" : this.text;
    }

    public int length() {
        return this.text.length();
    }

    public String getText() {
        return this.text;
    }

    public boolean isEmpty() {
        return this.text.isEmpty();
    }

    public int getLine() {
        return line;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public Type getType(){
        return this.type;
    }

    public boolean isDelimiter(){
        return this.type == Type.DELIMITER;
    }

    public boolean isBlock(){
        return this.type == Type.BLOCK;
    }

    public boolean isComment() {
        return this.type == Type.COMMENT;
    }

    public boolean isForLoop() {
        return this.type == Type.FOR_LOOP;
    }

    public boolean isAssignment(){
        return this.type == Type.ASSIGNMENT;
    }

    public boolean isText() {
        return this.type == Type.TEXT;
    }

    public boolean isContinuation() {
        return this.type == Type.CONTINUATION;
    }

    public boolean isVariable(){
        return this.type == Type.VARIABLE;
    }

    public boolean matches(Token other) {
        if(other == null){
            return false;
        }

        return matches(other.text);
    }

    public boolean matches(String text){
        return this.text.equalsIgnoreCase(text);
    }

    public Token extract(int start){
        return this.extract(start, this.text.length());
    }

    public Token extract(int start, int end) {
        String value = this.text.substring(start, end);
        int newStartOffset = this.startOffset + start;
        int newEndOffset = this.startOffset + end;
        Type newType = value.isEmpty() ? Type.EMPTY : this.type;

        return new Token(value, this.line, newStartOffset, newEndOffset, newType);
    }

    public Token trim(Pattern pattern){
        Matcher m = pattern.matcher(this.getText());

        if (m.find()) {
            int start = 0;
            int end = this.text.length();

            if(m.start() == 0){
                start = m.end();
            }

            if(m.end() == this.text.length()){
                end = Math.max(start, m.start());
            }

            return this.extract(start, end);
        }

        return this.copy();
    }

    public Token extract(Pattern pattern){
        Matcher m = pattern.matcher(this.getText());

        if(m.find()){
            return this.extract(m.start(), m.end());
        }

        return Token.empty();
    }

    public Pair<Token, Token> splitLibrary(){
        List<String> particles = Arrays.asList(this.text.split("\\."));

        if(particles.size() == 1){
            return Pair.of(Token.empty(), this);
        }

        String library = particles.size() > 1 ? String.join(".", particles.subList(0, particles.size() - 1)) : "";
        Token libraryToken = this.extract(0, library.length());
        Token nameToken = this.extract(library.length() + 1);

        return Pair.of(libraryToken, nameToken);
    }

    public List<Token> split(String regex) {
        final Matcher matcher = Pattern.compile(regex).matcher(this.text);
        final List<Token> tokens = new ArrayList<>();

        int position = 0;

        while(matcher.find()){
            tokens.add(this.extract(position, matcher.start()));
            position = matcher.end();
        }

        if(tokens.isEmpty()){
            tokens.add(this.copy());
        }

        return tokens;
    }

    public Token replace(Token token, String value) {
        return replace(token.startOffset, token.endOffset, value);
    }

    public Token replace(int startOffset, int endOffset, String value){
        if(startOffset < this.startOffset){
            throw new IndexOutOfBoundsException(String.format("Start Offset [%d] can not be lower than token Start Offset [%d]", startOffset, this.startOffset));
        }

        if(endOffset > this.endOffset){
            throw new IndexOutOfBoundsException(String.format("End Offset [%d] can not be higher than token End Offset [%d]", endOffset, this.endOffset));
        }

        if(startOffset > endOffset){
            throw new InvalidArgumentException(String.format("End Offset [%d] cannot be lower than Start Offset [%d]", endOffset, startOffset));
        }

        final String newText = this.text.substring(0, startOffset - this.startOffset) + value + this.text.substring(endOffset - this.startOffset, this.endOffset - this.startOffset);
        return new Token(newText, this.line, this.startOffset, this.startOffset + newText.length(), this.type);
    }

    public static Token fromString(String text) {
        return new Token(text, -1, 0, text.length(), Type.TEXT);
    }

    public static Token empty(){
        return new Token("", -1, -1, -1, Type.EMPTY);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (this.getClass() != other.getClass()) return false;

        Token that = (Token) other;

        return this.line == that.line
                && this.startOffset == that.startOffset
                && this.endOffset == that.endOffset
                && this.type == that.type
                && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + line;
        result = 31 * result + startOffset;
        result = 31 * result + endOffset;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Token other) {
        if(this.line != other.line){
            return Integer.compare(this.line, other.line);
        }

        if(this.startOffset != other.startOffset){
            return Integer.compare(this.startOffset, other.startOffset);
        }

        return Integer.compare(this.endOffset, other.endOffset);
    }
}
