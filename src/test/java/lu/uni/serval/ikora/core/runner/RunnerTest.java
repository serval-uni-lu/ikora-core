/*
 *
 *     Copyright © 2022 University of Luxembourg
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
package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.runner.report.Report;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RunnerTest {
    @Test
    void testSimplePassingCallWithLiteral() throws Exception {
        final Project project = Helpers.compileProject("projects/runner/simple/literal.ikora");
        final Runner runner = new Runner(project);
        final Report report = runner.execute();

        assertEquals(1, report.getNumberPassingTests());
        assertEquals(0, report.getNumberFailingTests());
        assertEquals(0, report.getNumberIgnoredTests());
    }

    @Test
    void testSimplePassingCallWithVariable() throws Exception {
        final Project project = Helpers.compileProject("projects/runner/simple/variable.ikora");
        final Runner runner = new Runner(project);
        final Report report = runner.execute();

        assertEquals(1, report.getNumberPassingTests());
        assertEquals(0, report.getNumberFailingTests());
        assertEquals(0, report.getNumberIgnoredTests());
    }

    @Test
    void testSimplePassingCallWithComposedVariable() throws Exception {
        final Project project = Helpers.compileProject("projects/runner/simple/composed-variable.ikora");
        final Runner runner = new Runner(project);
        final Report report = runner.execute();

        assertEquals(1, report.getNumberPassingTests());
        assertEquals(0, report.getNumberFailingTests());
        assertEquals(0, report.getNumberIgnoredTests());
    }
}
