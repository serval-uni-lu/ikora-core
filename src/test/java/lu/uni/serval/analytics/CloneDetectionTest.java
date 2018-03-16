package lu.uni.serval.analytics;

import static org.junit.Assert.assertEquals;

import lu.uni.serval.Globals;
import lu.uni.serval.utils.tree.TreeNode;
import lu.uni.serval.utils.tree.TreeNodeDataTest;
import org.junit.Test;

public class CloneDetectionTest {

    @Test
    public void checkSameTreeDifferentKeywordIsZero(){
        TreeNode tree1 = new TreeNode(new TreeNodeDataTest("a keyword"));
        tree1.addChild(new TreeNodeDataTest("1"));
        tree1.addChild(new TreeNodeDataTest("2"));

        TreeNode tree2 = new TreeNode(new TreeNodeDataTest("another keyword"));
        tree2.addChild(new TreeNodeDataTest("1"));
        tree2.addChild(new TreeNodeDataTest("2"));

        CloneDetection cloneDetection = new CloneDetection();
        CloneIndex index = cloneDetection.computeCloneIndex(tree1, tree2);

        assertEquals(0.0, index.getTreeRatio(), Globals.delta);
    }

    @Test
    public void checkSameKeywordDifferentTreeIsOne(){
        TreeNode tree1 = new TreeNode(new TreeNodeDataTest("keyword"));
        tree1.addChild(new TreeNodeDataTest("4"));
        tree1.addChild(new TreeNodeDataTest("5"));

        TreeNode tree2 = new TreeNode(new TreeNodeDataTest("keyword"));
        tree2.addChild(new TreeNodeDataTest("1"));
        tree2.addChild(new TreeNodeDataTest("2"));

        CloneDetection cloneDetection = new CloneDetection();
        CloneIndex index = cloneDetection.computeCloneIndex(tree1, tree2);

        assertEquals(0.0, index.getTreeRatio(), Globals.delta);
    }
}
