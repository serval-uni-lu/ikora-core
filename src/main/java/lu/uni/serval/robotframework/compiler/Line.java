package lu.uni.serval.robotframework.compiler;

public class Line {
    private String text;
    private int number;

    public Line(String text, int number) {
        this.text = text;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    public boolean isValid() {
        return text != null;
    }

    public boolean isEmpty() {
        return text.trim().isEmpty();
    }

    public String[] tokenize(){
        String tokens = this.text.replaceAll("\\s\\s(\\s*)", "\t");
        tokens = tokens.replaceAll("\\\\t", "\t");
        return tokens.split("\t");
    }

    public int getIndentSize() {
        String s = this.text.replaceAll("\\s\\s(\\s*)", "\t");
        s = s.replaceAll("\\\\\\t", "\t");

        int i = 0;

        for (; i < s.length(); i++){
            if(s.charAt(i) != '\t'){
                break;
            }
        }

        return i;
    }

    public boolean isInBlock(Line line) {
        if(Utils.ignore(this)){
            return true;
        }

        return getIndentSize() == line.getIndentSize() + 1;
    }
}
