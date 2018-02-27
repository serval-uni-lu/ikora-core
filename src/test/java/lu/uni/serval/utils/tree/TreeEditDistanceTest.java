package lu.uni.serval.utils.tree;

import static org.junit.Assert.*;
import org.junit.Test;

public class TreeEditDistanceTest {
    private final static double delta = 0.0001;

    @Test
    public void checkSimpleDeletionEdit() {
        TreeNode tree1 = new TreeNode(new TreeNodeDataTest("0"));
        tree1.addChild(new TreeNodeDataTest("1"));
        tree1.addChild(new TreeNodeDataTest("2"));

        TreeNode tree2 = new TreeNode(new TreeNodeDataTest("0"));
        tree2.addChild(new TreeNodeDataTest("1"));

        TreeEditDistance editDistance = new TreeEditDistance(new SimpleEditScore());

        double distance = editDistance.calculate(tree1, tree2);

        assertEquals(1.0, distance, delta);
    }

    @Test
    public void checkSimpleInsertionEdit() {
        TreeNode tree1 = new TreeNode(new TreeNodeDataTest("0"));
        tree1.addChild(new TreeNodeDataTest("1"));

        TreeNode tree2 = new TreeNode(new TreeNodeDataTest("0"));
        tree2.addChild(new TreeNodeDataTest("1"));
        tree2.addChild(new TreeNodeDataTest("2"));

        TreeEditDistance editDistance = new TreeEditDistance(new SimpleEditScore());

        double distance = editDistance.calculate(tree1, tree2);

        assertEquals(1.0, distance, delta);
    }

    @Test
    public void checkSimpleReplaceEdit() {
        TreeNode tree1 = new TreeNode(new TreeNodeDataTest("0"));
        tree1.addChild(new TreeNodeDataTest("1"));
        tree1.addChild(new TreeNodeDataTest("3"));

        TreeNode tree2 = new TreeNode(new TreeNodeDataTest("0"));
        tree2.addChild(new TreeNodeDataTest("1"));
        tree2.addChild(new TreeNodeDataTest("2"));

        TreeEditDistance editDistance = new TreeEditDistance(new SimpleEditScore());

        double distance = editDistance.calculate(tree1, tree2);

        assertEquals(1.0, distance, delta);
    }
}
