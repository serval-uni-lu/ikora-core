/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.analytics;

import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.Step;
import lu.uni.serval.ikora.core.model.TestCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.core.Helpers;

import static org.junit.jupiter.api.Assertions.*;

class KeywordStatisticsTest {
    static Project project;

    @BeforeAll
    static void setup(){
        project = Helpers.compileProject("projects/web-demo", true);
    }

    @Test
    void testSequenceSizeForTestCase(){
        final TestCase testCase = project.findTestCase(null, "Valid Login").iterator().next();
        int numberSteps = KeywordStatistics.getStatementCount(testCase);

        assertEquals(17, numberSteps);
    }

    @Test
    void testSequenceSizeForStep(){
        final  TestCase testCase = project.findTestCase(null, "Valid Login").iterator().next();
        final Step step = testCase.getStep(0);
        int numberSteps = KeywordStatistics.getSequenceSize(step);

        assertEquals(4, numberSteps);
    }
}
