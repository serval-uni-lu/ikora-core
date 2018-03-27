package lu.uni.serval.utils.tree;

import lu.uni.serval.Globals;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LevenshteinEditScoreTest {

    @Test
    public void checkReplaceWithDifferentLabels(){
        TreeNode node1 = new TreeNode(new TreeNodeDataTest("First String"), false);
        TreeNode node2 = new TreeNode(new TreeNodeDataTest("Second String"), false);
        LevenshteinEditScore editScore = new LevenshteinEditScore();

        double score = editScore.replace(node1, node2);

        assertEquals(0.461538461538, score, Globals.delta);
    }

}
