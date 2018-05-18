package lu.uni.serval.utils.tree;

import static org.junit.Assert.*;

import lu.uni.serval.Globals;
import lu.uni.serval.utils.exception.DuplicateNodeException;
import org.junit.Test;

public class TreeEditDistanceTest {

    @Test
    public void checkSimpleDeletionEdit() {
        LabelTreeNode tree1 = new LabelTreeNode(new TreeNodeDataTest("0"));
        tree1.add(new TreeNodeDataTest("1"));
        tree1.add(new TreeNodeDataTest("2"));

        LabelTreeNode tree2 = new LabelTreeNode(new TreeNodeDataTest("0"));
        tree2.add(new TreeNodeDataTest("1"));

        TreeEditDistance editDistance = new TreeEditDistance(1.0, 1.0, 1.0);

        double distance = editDistance.distance(tree1, tree2);

        assertEquals(1.0, distance, Globals.delta);
    }

    @Test
    public void checkSimpleInsertionEdit() {
        LabelTreeNode tree1 = new LabelTreeNode(new TreeNodeDataTest("0"));
        tree1.add(new TreeNodeDataTest("1"));

        LabelTreeNode tree2 = new LabelTreeNode(new TreeNodeDataTest("0"));
        tree2.add(new TreeNodeDataTest("1"));
        tree2.add(new TreeNodeDataTest("2"));

        TreeEditDistance editDistance = new TreeEditDistance(1.0, 1.0, 1.0);

        double distance = editDistance.distance(tree1, tree2);

        assertEquals(1.0, distance, Globals.delta);
    }

    @Test
    public void checkSimpleReplaceEdit() {
        LabelTreeNode tree1 = new LabelTreeNode(new TreeNodeDataTest("0"));
        tree1.add(new TreeNodeDataTest("1"));
        tree1.add(new TreeNodeDataTest("3"));

        LabelTreeNode tree2 = new LabelTreeNode(new TreeNodeDataTest("0"));
        tree2.add(new TreeNodeDataTest("1"));
        tree2.add(new TreeNodeDataTest("2"));

        TreeEditDistance editDistance = new TreeEditDistance(1.0, 1.0, 1.0);

        double distance = editDistance.distance(tree1, tree2);

        assertEquals(1.0, distance, Globals.delta);
    }
}
