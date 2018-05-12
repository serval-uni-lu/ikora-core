package lu.uni.serval.analytics;

import static org.junit.Assert.assertEquals;

import lu.uni.serval.Globals;
import lu.uni.serval.utils.exception.DuplicateNodeException;
import lu.uni.serval.utils.tree.LabelTreeNode;
import lu.uni.serval.utils.tree.TreeNodeDataTest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CloneDetectionTest {

    private static List<LabelTreeNode> trees;

    private static List<LabelTreeNode> getTrees(){
        if(!(trees == null)){
            return trees;
        }

        trees = new ArrayList<>();

        LabelTreeNode tree0 = new LabelTreeNode(new TreeNodeDataTest("a keyword"));
        tree0.add(new TreeNodeDataTest("1"));
        tree0.add(new TreeNodeDataTest("2"));

        trees.add(tree0);

        LabelTreeNode tree1 = new LabelTreeNode(new TreeNodeDataTest("another keyword"));
        tree1.add(new TreeNodeDataTest("1"));
        tree1.add(new TreeNodeDataTest("2"));

        trees.add(tree1);

        LabelTreeNode tree2 = new LabelTreeNode(new TreeNodeDataTest("keyword"));
        tree2.add(new TreeNodeDataTest("4"));
        tree2.add(new TreeNodeDataTest("5"));

        trees.add(tree2);

        LabelTreeNode tree3 = new LabelTreeNode(new TreeNodeDataTest("keyword"));
        tree3.add(new TreeNodeDataTest("1"));
        tree3.add(new TreeNodeDataTest("2"));

        trees.add(tree3);

        LabelTreeNode tree4 = new LabelTreeNode(new TreeNodeDataTest("keyword last"));
        tree4.add(new TreeNodeDataTest("4"));
        tree4.add(new TreeNodeDataTest("2"));

        trees.add(tree4);

        return trees;
    }

    private static LabelTreeNode getTree(int index){
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
