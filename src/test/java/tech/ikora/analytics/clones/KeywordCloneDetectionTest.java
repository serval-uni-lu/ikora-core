package tech.ikora.analytics.clones;

import org.junit.jupiter.api.Test;
import tech.ikora.Helpers;
import tech.ikora.model.KeywordDefinition;
import tech.ikora.model.Project;

import static org.junit.jupiter.api.Assertions.*;

class KeywordCloneDetectionTest {
    @Test
    void testCloneDetectionInSimpleFile(){
        Project project = Helpers.compileProject("robot/clones.robot", true);
        Clones<KeywordDefinition> clones = KeywordCloneDetection.findClones(project);

        assertEquals(2, clones.size(Clones.Type.TYPE_1));
        assertEquals(5, clones.size(Clones.Type.TYPE_2));
        assertEquals(4, clones.size(Clones.Type.TYPE_3));
    }
}