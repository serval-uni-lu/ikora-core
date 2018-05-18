package lu.uni.serval.utils.tree;

import lu.uni.serval.Globals;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LevenshteinEditScoreTest {

    @Test
    public void checkReplaceWithDifferentLabels(){
        LabelTreeNode node1 = new LabelTreeNode(new TreeNodeDataTest("First String"));
        LabelTreeNode node2 = new LabelTreeNode(new TreeNodeDataTest("Second String"));
        LevenshteinEditScore editScore = new LevenshteinEditScore();

        double score = editScore.replace(node1, node2);

        assertEquals(0.461538461538, score, Globals.delta);
    }

}
