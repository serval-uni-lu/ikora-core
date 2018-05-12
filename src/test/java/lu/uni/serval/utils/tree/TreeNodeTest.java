package lu.uni.serval.utils.tree;

import static org.junit.Assert.*;

import lu.uni.serval.utils.exception.DuplicateNodeException;
import org.junit.Test;

public class TreeNodeTest {

    @Test
    public void checkWhenAddOneNodeItIsTheRoot(){
        LabelTreeNode node = new LabelTreeNode(new TreeNodeDataTest("0"));

        assertEquals(node, node.getRoot());
    }

    @Test
    public void checkWhenAddOneNodeLevelIsZero(){
        LabelTreeNode node = new LabelTreeNode(new TreeNodeDataTest("0"));

        assertEquals(0, node.getLevel());
    }

    @Test
    public void checkWhenAddChildLevelIsOne(){
        LabelTreeNode root = new LabelTreeNode(new TreeNodeDataTest("0"));
        LabelTreeNode child = root.add(new TreeNodeDataTest("1"));

        assertEquals(1, child.getLevel());
    }
}
