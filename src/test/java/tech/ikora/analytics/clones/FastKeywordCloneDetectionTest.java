package tech.ikora.analytics.clones;

import org.junit.jupiter.api.Test;
import tech.ikora.Helpers;
import tech.ikora.model.KeywordDefinition;
import tech.ikora.model.Project;

import static org.junit.jupiter.api.Assertions.*;

class FastKeywordCloneDetectionTest {
    @Test
    void testCloneDetectionInSimpleFile(){
        Project project = Helpers.compileProject("robot/clones.robot", true);
        Clones<KeywordDefinition> clones = FastKeywordCloneDetection.findClones(project);

        assertEquals(2, clones.size(Clone.Type.TYPE_1));
        assertEquals(5, clones.size(Clone.Type.TYPE_2));
        assertEquals(4, clones.size(Clone.Type.TYPE_3));
    }
}