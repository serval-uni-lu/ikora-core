package lu.uni.serval.analytics;

import static org.junit.Assert.assertEquals;

import lu.uni.serval.Globals;
import lu.uni.serval.utils.exception.DuplicateNodeException;
import lu.uni.serval.utils.tree.TreeNode;
import lu.uni.serval.utils.tree.TreeNodeDataTest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CloneDetectionTest {

    private static List<TreeNode> trees;

    private static List<TreeNode> getTrees(){
        if(!(trees == null)){
            return trees;
        }

        trees = new ArrayList<>();

        TreeNode tree0 = new TreeNode(new TreeNodeDataTest("a keyword"), false);

        try {
            tree0.addChild(new TreeNodeDataTest("1"));
            tree0.addChild(new TreeNodeDataTest("2"));
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }

        trees.add(tree0);

        TreeNode tree1 = new TreeNode(new TreeNodeDataTest("another keyword"), false);

        try {
            tree1.addChild(new TreeNodeDataTest("1"));
            tree1.addChild(new TreeNodeDataTest("2"));
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }

        trees.add(tree1);

        TreeNode tree2 = new TreeNode(new TreeNodeDataTest("keyword"), false);

        try {
            tree2.addChild(new TreeNodeDataTest("4"));
            tree2.addChild(new TreeNodeDataTest("5"));
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }

        trees.add(tree2);

        TreeNode tree3 = new TreeNode(new TreeNodeDataTest("keyword"), false);

        try {
            tree3.addChild(new TreeNodeDataTest("1"));
            tree3.addChild(new TreeNodeDataTest("2"));
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }

        trees.add(tree3);

        TreeNode tree4 = new TreeNode(new TreeNodeDataTest("keyword last"), false);

        try {
            tree4.addChild(new TreeNodeDataTest("4"));
            tree4.addChild(new TreeNodeDataTest("2"));
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }

        trees.add(tree4);

        return trees;
    }

    private static TreeNode getTree(int index){
        return getTrees().get(index);
    }

    @BeforeClass
    public static void prepareCloneIndex(){
        CloneIndex.setTreeThreshold(0.7);
        CloneIndex.setKeywordThreshold(0.7);
    }

    @Test
    public void checkSynonymTreeIndexIsOne(){
        CloneDetection cloneDetection = new CloneDetection();

        try {
            CloneIndex index = cloneDetection.computeCloneIndex(getTree(0), getTree(1));
            assertEquals(1.0, index.getTreeRatio(), Globals.delta);
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void checkHomonymTreeIndexIsZero(){
        CloneDetection cloneDetection = new CloneDetection();

        try {
            CloneIndex index = cloneDetection.computeCloneIndex(getTree(2), getTree(3));
            assertEquals(0.0, index.getTreeRatio(), Globals.delta);
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkHomonymTreeIndexIsHalf(){
        CloneDetection cloneDetection = new CloneDetection();

        try {
            CloneIndex index = cloneDetection.computeCloneIndex(getTree(2), getTree(4));
            assertEquals(0.5, index.getTreeRatio(), Globals.delta);
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkFindClones(){
        CloneDetection cloneDetection = new CloneDetection();

        try {
            CloneResults results = cloneDetection.findClones(new HashSet<>(getTrees()));
            assertEquals(1, results.getSame().size());
            assertEquals(2, results.getHomonym().size());
            assertEquals(2, results.getSynonym().size());
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }
    }
}
