package lu.uni.serval.utils.tree;

public class ScoreElement {
    public final double score;
    public final EditOperation operation;

    public ScoreElement(double score, EditOperation operation){
        this.score = score;
        this.operation = operation;
    }
}
