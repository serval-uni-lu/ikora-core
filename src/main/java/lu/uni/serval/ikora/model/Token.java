package lu.uni.serval.ikora.model;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
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

    public Token clone(){
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

        return this.text.contains(name);
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

    public boolean equalsIgnorePosition(Token name) {
        return name.text.equalsIgnoreCase(this.text);
    }

    public Token extract(int start){
        return this.extract(start, this.text.length());
    }

    public Token extract(int start, int end) {
        String value = this.text.substring(start, end);
        int startOffset = this.startOffset + start;
        int endOffset = this.startOffset + end;
        Type type = value.isEmpty() ? Type.EMPTY : this.type;

        return new Token(value, this.line, startOffset, endOffset, type);
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
                end = m.start();
            }

            return this.extract(start, end);
        }

        return this.clone();
    }

    public Token extract(Pattern pattern){
        Matcher m = pattern.matcher(this.getText());

        if(m.find()){
            return this.extract(m.start(), m.end());
        }

        return this.clone();
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

    public static Token fromString(String text) {
        return new Token(text, -1, 0, text.length(), Type.TEXT);
    }

    public static Token empty(){
        return new Token("", -1, -1, -1, Type.EMPTY);
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
