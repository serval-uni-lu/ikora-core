package lu.uni.serval.analytics;

import static org.junit.Assert.assertEquals;

import lu.uni.serval.Globals;
import lu.uni.serval.utils.tree.TreeNode;
import lu.uni.serval.utils.tree.TreeNodeDataTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CloneDetectionTest {

    private static List<TreeNode> trees;

    private static List<TreeNode> getTrees(){
        if(!(trees == null)){
            return trees;
        }

        trees = new ArrayList<TreeNode>();

        TreeNode tree0 = new TreeNode(new TreeNodeDataTest("a keyword"));
        tree0.addChild(new TreeNodeDataTest("1"));
        tree0.addChild(new TreeNodeDataTest("2"));
        trees.add(tree0);

        TreeNode tree1 = new TreeNode(new TreeNodeDataTest("another keyword"));
        tree1.addChild(new TreeNodeDataTest("1"));
        tree1.addChild(new TreeNodeDataTest("2"));
        trees.add(tree1);

        TreeNode tree2 = new TreeNode(new TreeNodeDataTest("keyword"));
        tree2.addChild(new TreeNodeDataTest("4"));
        tree2.addChild(new TreeNodeDataTest("5"));
        trees.add(tree2);

        TreeNode tree3 = new TreeNode(new TreeNodeDataTest("keyword"));
        tree3.addChild(new TreeNodeDataTest("1"));
        tree3.addChild(new TreeNodeDataTest("2"));
        trees.add(tree3);

        TreeNode tree4 = new TreeNode(new TreeNodeDataTest("keyword last"));
        tree4.addChild(new TreeNodeDataTest("4"));
        tree4.addChild(new TreeNodeDataTest("2"));
        trees.add(tree4);

        return trees;
    }

    private static TreeNode getTree(int index){
        return getTrees().get(index);
    }

    @Test
    public void checkSynonymTreeIndexIsOne(){
        CloneDetection cloneDetection = new CloneDetection();
        CloneIndex index = cloneDetection.computeCloneIndex(getTree(0), getTree(1));

        assertEquals(1.0, index.getTreeRatio(), Globals.delta);
    }

    @Test
    public void checkHomonymTreeIndexIsZero(){
        CloneDetection cloneDetection = new CloneDetection();
        CloneIndex index = cloneDetection.computeCloneIndex(getTree(2), getTree(3));

        assertEquals(0.0, index.getTreeRatio(), Globals.delta);
    }

    @Test
    public void checkHomonymTreeIndexIsHalf(){
        CloneDetection cloneDetection = new CloneDetection();
        CloneIndex index = cloneDetection.computeCloneIndex(getTree(2), getTree(4));

        assertEquals(0.5, index.getTreeRatio(), Globals.delta);
    }

    @Test
    public void checkFindClones(){
        CloneDetection cloneDetection = new CloneDetection();
        CloneResults results = cloneDetection.findClones(getTrees());

        assertEquals(2, results.getSame().size());
        assertEquals(3, results.getHomonym().size());
        assertEquals(3, results.getSynonym().size());
    }
}
