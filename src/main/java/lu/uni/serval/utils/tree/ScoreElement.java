package lu.uni.serval.utils.tree;

import java.util.List;

public class ScoreElement {
    public final double score;
    public final EditOperation operation;
    public final List<EditAction> actions;

    public ScoreElement(double score, EditOperation operation, List<EditAction> actions){
        this.score = score;
        this.operation = operation;
        this.actions = actions;
    }
}
