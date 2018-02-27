package lu.uni.serval.utils.tree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LevenshteinEditScoreTest {
    private final static double delta = 0.0001;

    @Test
    public void checkReplaceWithDifferentLabels(){
        TreeNode node1 = new TreeNode(new TreeNodeDataTest("First String"));
        TreeNode node2 = new TreeNode(new TreeNodeDataTest("Second String"));
        LevenshteinEditScore editScore = new LevenshteinEditScore();

        double score = editScore.replace(node1, node2);

        assertEquals(0.461538461538, score, delta);
    }

}
