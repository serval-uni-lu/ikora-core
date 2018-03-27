package lu.uni.serval.utils.tree;

import static org.junit.Assert.*;

import lu.uni.serval.Globals;
import lu.uni.serval.utils.exception.DuplicateNodeException;
import org.junit.Test;

public class TreeEditDistanceTest {

    @Test
    public void checkSimpleDeletionEdit() {
        TreeNode tree1 = new TreeNode(new TreeNodeDataTest("0"), false);

        try {
            tree1.addChild(new TreeNodeDataTest("1"));
            tree1.addChild(new TreeNodeDataTest("2"));
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }

        TreeNode tree2 = new TreeNode(new TreeNodeDataTest("0"), false);

        try {
            tree2.addChild(new TreeNodeDataTest("1"));
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }

        TreeEditDistance editDistance = new TreeEditDistance(new SimpleEditScore());

        double distance = editDistance.distance(tree1, tree2);

        assertEquals(1.0, distance, Globals.delta);
    }

    @Test
    public void checkSimpleInsertionEdit() {
        TreeNode tree1 = new TreeNode(new TreeNodeDataTest("0"), false);

        try {
            tree1.addChild(new TreeNodeDataTest("1"));
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }

        TreeNode tree2 = new TreeNode(new TreeNodeDataTest("0"), false);


        try {
            tree2.addChild(new TreeNodeDataTest("1"));
            tree2.addChild(new TreeNodeDataTest("2"));
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }

        TreeEditDistance editDistance = new TreeEditDistance(new SimpleEditScore());

        double distance = editDistance.distance(tree1, tree2);

        assertEquals(1.0, distance, Globals.delta);
    }

    @Test
    public void checkSimpleReplaceEdit() {
        TreeNode tree1 = new TreeNode(new TreeNodeDataTest("0"), false);

        try {
            tree1.addChild(new TreeNodeDataTest("1"));
            tree1.addChild(new TreeNodeDataTest("3"));
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }

        TreeNode tree2 = new TreeNode(new TreeNodeDataTest("0"), false);

        try {
            tree2.addChild(new TreeNodeDataTest("1"));
            tree2.addChild(new TreeNodeDataTest("2"));
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }


        TreeEditDistance editDistance = new TreeEditDistance(new SimpleEditScore());

        double distance = editDistance.distance(tree1, tree2);

        assertEquals(1.0, distance, Globals.delta);
    }
}
