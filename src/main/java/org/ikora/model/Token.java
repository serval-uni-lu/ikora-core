package org.ikora.model;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token {
    public enum Type{
        TEXT, DELIMITER, BLOCK, COMMENT, FOR_LOOP, ASSIGNMENT, KEYWORD, EMPTY
    }

    private Type type;

    private final int line;
    private final int startOffset;
    private final int endOffset;

    private final String value;

    public Token(String value, int line, int startOffset, int endOffset, Type type){
        this.value = value;
        this.line = line;
        this.startOffset = startOffset;
        this.endOffset = endOffset;

        this.type = type;
    }

    public Token clone(){
        return new Token(this.value, this.line, this.startOffset, this.endOffset, this.type);
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String getValue() {
        return this.value;
    }

    public boolean isEmpty() {
        return this.value.isEmpty();
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

    public boolean isDelimiter(){
        return this.type == Type.DELIMITER;
    }

    public boolean isBlock(){
        return this.type == Type.BLOCK;
    }

    public boolean isBlock(String name){
        if(this.type != Type.BLOCK){
            return false;
        }

        return this.value.contains(name);
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

    public boolean equalsValue(Token name) {
        return name.value.equalsIgnoreCase(this.value);
    }

    public Token extract(int start){
        return this.extract(start, this.value.length());
    }

    public Token extract(int start, int end) {
        String value = this.value.substring(start, end);
        int startOffset = this.startOffset + start;
        int endOffset = this.startOffset + end;
        Type type = value.isEmpty() ? Type.EMPTY : this.type;

        return new Token(value, this.line, startOffset, endOffset, type);
    }

    public Token trim(Pattern pattern){
        Matcher m = pattern.matcher(this.getValue());

        if (m.find()) {
            int start = 0;
            int end = this.value.length();

            if(m.start() == 0){
                start = m.end();
            }

            if(m.end() == this.value.length()){
                end = m.start();
            }

            return this.extract(start, end);
        }

        return this.clone();
    }

    public Pair<Token, Token> splitLibrary(){
        List<String> particles = Arrays.asList(this.value.split("\\."));

        if(particles.size() == 1){
            return Pair.of(Token.empty(), this);
        }

        String library = particles.size() > 1 ? String.join(".", particles.subList(0, particles.size() - 1)) : "";
        Token libraryToken = this.extract(0, library.length());
        Token nameToken = this.extract(library.length() + 1);

        return Pair.of(libraryToken, nameToken);
    }

    public static Token fromString(String text) {
        return new Token(text, -1, 0, text.length(), Type.TEXT);
    }

    public static Token empty(){
        return new Token("", -1, -1, -1, Type.EMPTY);
    }
}
