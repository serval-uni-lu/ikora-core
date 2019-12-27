package org.ikora.analytics;

import org.ikora.Helpers;
import org.ikora.model.Project;
import org.ikora.model.UserKeyword;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class CloneDetectionTest {
    @Test
    void testCloneDetectionInSimpleFile(){
        Project project = Helpers.compileProject("robot/clones.robot", true);
        Clones<UserKeyword> clones = CloneDetection.findClones(project, UserKeyword.class);

        assertEquals(2, clones.size(Clone.Type.TYPE_1));
        assertEquals(3, clones.size(Clone.Type.TYPE_2));
        assertEquals(4, clones.size(Clone.Type.TYPE_3));
    }
}
