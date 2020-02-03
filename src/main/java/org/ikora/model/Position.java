package org.ikora.model;

import org.ikora.builder.Line;

public class Position {
    private final Mark startMark;
    private final Mark endMark;

    public Position(Mark startMark, Mark endMark) {
        this.startMark = startMark;
        this.endMark = endMark;
    }

    public Mark getStartMark() {
        return startMark;
    }

    public Mark getEndMark() {
        return endMark;
    }

    public boolean isValid(){
        return startMark != null && endMark != null;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", startMark.toString(), endMark.toString());
    }

    public static Position createInvalid(){
        return new Position(new Mark(-1, -1), new Mark(-1, -1));
    }

    public static Position fromTokens(Token start, Token end) {
        if(start == null || end == null){
            return Position.createInvalid();
        }

        Mark startMark = new Mark(start.getLine(), start.getStartOffset());
        Mark endMark = new Mark(end.getLine(), end.getEndOffset());

        return new Position(startMark, endMark);
    }

    public static Position fromTokens(Tokens tokens) {
        return Position.fromTokens(tokens.first(), tokens.last());
    }

    public static Position fromToken(Token token){
        return Position.fromTokens(token, token);
    }

    public static Position fromLine(Line line) {
        if(line == null){
            return Position.createInvalid();
        }

        Mark startMark = new Mark(line.getNumber(), 0);
        Mark endMark = new Mark(line.getNumber(), line.getText().length());

        return new Position(startMark, endMark);
    }
}
