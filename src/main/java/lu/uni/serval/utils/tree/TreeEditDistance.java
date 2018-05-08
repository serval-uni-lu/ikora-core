package lu.uni.serval.utils.tree;

import lu.uni.serval.utils.CompareCache;

import java.util.ArrayList;
import java.util.List;

public class TreeEditDistance implements TreeDistance {
    private final EditScore score;

    public TreeEditDistance(EditScore score) {
        if(score == null) {
            throw new NullPointerException();
        }

        this.score = score;
    }

    public double distance(TreeNode tree1, TreeNode tree2) {
        if (tree1 == null || tree2 == null) {
            throw new NullPointerException();
        }

        CompareCache<TreeView, ScoreElement> memory = new CompareCache<>();
        List<EditAction> actions = new ArrayList<>();

        return execute(memory, actions, new TreeView(tree1), new TreeView(tree2));
    }

    public double index(TreeNode tree1, TreeNode tree2) {
        if(tree1.getSize() == 0 && tree2.getSize() == 0) {
            return 0;
        }

        double distance = distance(tree1, tree2);
        double size = score.size(tree1, tree2);

        return distance / size;
    }

    public List<EditAction> differences(TreeNode tree1, TreeNode tree2){
        if(tree1.getSize() == 0 && tree2.getSize() == 0){
            return new ArrayList<>();
        }

        CompareCache<TreeView, ScoreElement> memory = new CompareCache<>();
        List<EditAction> actions = new ArrayList<>();

        execute(memory, actions, new TreeView(tree1), new TreeView(tree2));

        return actions;
    }

    private double execute(CompareCache<TreeView, ScoreElement> memory, List<EditAction> actions, TreeView tree1, TreeView tree2) {

        if (tree1 == null && tree2 == null) {
            return 0.0;
        }

        if (checkCache(memory, actions, tree1, tree2)) {
            return memory.getScore(tree1, tree2).score;
        }

        double score;
        EditOperation operation;
        List<EditAction> subtreeActions = new ArrayList<>();

        if (tree1 == null) {
            score = calculateInsertScore(memory, subtreeActions, null, tree2);
            operation = EditOperation.Insert;
        }
        else if (tree2 == null) {
            score = calculateDeleteScore(memory, subtreeActions, tree1, null);
            operation = EditOperation.Delete;
        }
        else {

            List<EditAction> replaceActions = new ArrayList<>();
            List<EditAction> deleteActions = new ArrayList<>();
            List<EditAction> insertActions = new ArrayList<>();

            double replace = calculateReplaceScore(memory, replaceActions, tree1, tree2);
            double delete = calculateDeleteScore(memory, deleteActions, tree1, tree2);
            double insert = calculateInsertScore(memory, insertActions, tree1, tree2);

            if(replace < delete && replace < insert) {
                score = replace;
                operation = EditOperation.Replace;
                if(replace > 0){
                    subtreeActions = replaceActions;
                }

            }
            else if (delete < insert) {
                score = delete;
                operation = EditOperation.Delete;
                subtreeActions = deleteActions;
            }
            else {
                score = insert;
                operation = EditOperation.Insert;
                subtreeActions = insertActions;
            }
        }

        actions.addAll(subtreeActions);
        memory.set(tree1, tree2, new ScoreElement(score, operation, subtreeActions));
        return score;
    }

    private boolean checkCache(CompareCache<TreeView, ScoreElement> memory, List<EditAction> actions, TreeView tree1, TreeView tree2) {
        if(memory.isCached(tree1, tree2)) {
            actions.addAll(memory.getScore(tree1, tree2).actions);
            return true;
        }

        return false;
    }

    private double calculateReplaceScore(CompareCache<TreeView, ScoreElement> memory, List<EditAction> actions, TreeView tree1, TreeView tree2) {
        double s1 = execute(memory, actions, tree1.getInside(), tree2.getInside());
        double s2 = execute(memory, actions,  tree1.getOutside(), tree2.getOutside());
        double replaceScore = score.replace(tree1.head(), tree2.head());

        if(replaceScore > 0){
            EditAction action = new EditAction(EditOperation.Replace, tree1.head(), tree2.head());
            actions.add(action);
        }

        return s1 + s2 + replaceScore;
    }

    private double calculateDeleteScore(CompareCache<TreeView, ScoreElement> memory, List<EditAction> actions, TreeView tree1, TreeView tree2) {
        EditAction action = new EditAction(EditOperation.Delete, getHead(tree1), getHead(tree2));
        actions.add(action);

        if(tree2 == null){
            return score.insert(getHead(tree1), getHead(tree2));
        }

        return execute(memory, actions, tree1.deleteHead(), tree2) + score.delete(getHead(tree2), getHead(tree2));
    }

    private double calculateInsertScore(CompareCache<TreeView, ScoreElement> memory, List<EditAction> actions, TreeView tree1, TreeView tree2) {
        EditAction action = new EditAction(EditOperation.Insert, getHead(tree1), getHead(tree2));
        actions.add(action);

        if(tree1 == null){
            return score.insert(getHead(tree1), getHead(tree2));
        }

        return execute(memory, actions, tree1, tree2.deleteHead()) + score.insert(getHead(tree1), getHead(tree2));
    }

    private TreeNode getHead(TreeView tree){
        return tree != null ? tree.head() : null;
    }
}
