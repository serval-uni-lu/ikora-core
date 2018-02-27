package lu.uni.serval.utils.tree;

import static org.junit.Assert.*;
import org.junit.Test;

public class TreeNodeTest {

    @Test
    public void checkWhenAddOneNodeItIsTheRoot(){
        TreeNode node = new TreeNode(new TreeNodeDataTest("0"));

        assertEquals(node, node.getRoot());
    }

    @Test
    public void checkWhenAddOneNodeLevelIsZero(){
        TreeNode node = new TreeNode(new TreeNodeDataTest("0"));

        assertEquals(0, node.getLevel());
    }

    @Test
    public void checkWhenAddChildLevelIsOne(){
        TreeNode root = new TreeNode(new TreeNodeDataTest("0"));

        TreeNode child = root.addChild(new TreeNodeDataTest("1"));

        assertEquals(1, child.getLevel());
    }
}
