package lu.uni.serval.utils.tree;

import static org.junit.Assert.*;

import lu.uni.serval.utils.exception.DuplicateNodeException;
import org.junit.Test;

public class TreeNodeTest {

    @Test
    public void checkWhenAddOneNodeItIsTheRoot(){
        TreeNode node = new TreeNode(new TreeNodeDataTest("0"), false);

        assertEquals(node, node.getRoot());
    }

    @Test
    public void checkWhenAddOneNodeLevelIsZero(){
        TreeNode node = new TreeNode(new TreeNodeDataTest("0"), false);

        assertEquals(0, node.getLevel());
    }

    @Test
    public void checkWhenAddChildLevelIsOne(){
        TreeNode root = new TreeNode(new TreeNodeDataTest("0"), false);

        TreeNode child = null;
        try {
            child = root.addChild(new TreeNodeDataTest("1"));
        } catch (DuplicateNodeException e) {
            e.printStackTrace();
        }

        assertEquals(1, child.getLevel());
    }
}
