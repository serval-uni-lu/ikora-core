package org.ikora.builder;

public class Token {
    public enum Type{
        TEXT, DELIMITER, BLOCK, COMMENT, FOR_LOOP, ASSIGNMENT, KEYWORD, VARIABLE
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

    public String getValue() {
        return this.value;
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
}
