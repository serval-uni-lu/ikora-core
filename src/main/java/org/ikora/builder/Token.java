package org.ikora.builder;

public class Token {
    public enum Type{
        Text, Delimiter, Block, Comment, ForLoop, Assignment, Keyword, Variable
    }

    private Type type;

    private final int startOffset;
    private final int endOffset;

    private final String value;

    public Token(String value, int startOffset, int endOffset, Type type){
        this.value = value;
        this.startOffset = startOffset;
        this.endOffset = endOffset;

        this.type = type;
    }

    public String getValue() {
        return this.value;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public boolean isDelimiter(){
        return this.type == Type.Delimiter;
    }

    public boolean isBlock(){
        return this.type == Type.Block;
    }

    public boolean isBlock(String name){
        if(this.type != Type.Block){
            return false;
        }

        return this.value.contains(name);
    }

    public boolean isComment() {
        return this.type == Type.Comment;
    }

    public boolean isForLoop() {
        return this.type == Type.ForLoop;
    }

    public boolean isAssignment(){
        return this.type == Type.Assignment;
    }

    public boolean isText() {
        return this.type == Type.Text;
    }
}
