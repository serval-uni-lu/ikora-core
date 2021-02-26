package lu.uni.serval.ikora.core.builder;

public class Line {
    private String text;
    private int number;
    private boolean isComment;
    private boolean isEmpty;

    public Line(String text, int number, boolean isComment, boolean isEmpty) {
        this.text = text;
        this.number = number;
        this.isComment = isComment;
        this.isEmpty = isEmpty;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    public boolean isCode() {
        return !ignore();
    }

    public boolean isValid() {
        return text != null;
    }

    public boolean isComment() {
        return isComment;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean ignore() {
        return isComment || isEmpty;
    }


}
