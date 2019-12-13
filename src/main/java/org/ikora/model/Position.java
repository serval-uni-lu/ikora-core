package org.ikora.model;

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

    public Position merge(Position other) {
        if(this == other){
            return this;
        }

        Mark start = this.startMark.before(other.startMark) ? this.startMark : other.startMark;
        Mark end = this.endMark.after(other.endMark) ? this.endMark : other.endMark;

        return new Position(start, end);
    }
}
