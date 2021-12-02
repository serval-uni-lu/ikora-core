package lu.uni.serval.ikora.core.analytics.violations;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.model.*;

import java.util.ArrayList;
import java.util.List;

public class ViolationDetection {
    private ViolationDetection() {}

    public static List<Violation> detect(SourceFile file, Violation.Cause cause){
        List<Violation> violations = new ArrayList<>();

        for(TestCase testCase: file.getTestCases()){
            detect(cause, violations, testCase.getSteps());
        }

        for(UserKeyword userKeyword: file.getUserKeywords()){
            detect(cause, violations, userKeyword.getSteps());
        }

        return violations;
    }

    public static List<Violation> detect(Project project, Violation.Cause cause){
        List<Violation> violations = new ArrayList<>();

        for(SourceFile file: project.getSourceFiles()){
            violations.addAll(detect(file, cause));
        }

        return violations;
    }

    public static List<Violation> detect(Projects projects, Violation.Cause cause){
        List<Violation> violations = new ArrayList<>();

        for(Project project: projects){
            violations.addAll(detect(project, cause));
        }

        return violations;
    }

    private static void detect(Violation.Cause cause, List<Violation> violations, List<Step> steps) {
        for(Step step: steps){
            if(step instanceof ForLoop){
                detect(cause, violations, step.getSteps());
            }

            switch (cause){
                case MULTIPLE_DEFINITIONS:
                    duplicatedKeyword(step, violations);
                    break;
                case NO_DEFINITION_FOUND:
                case INFINITE_CALL_RECURSION:
                    throw new UnsupportedOperationException();
            }
        }
    }

    private static void duplicatedKeyword(Step step, List<Violation> violations) {
        step.getKeywordCall().ifPresent(call->{
            if(call.getAllPotentialKeywords(Link.Import.STATIC).size() > 1){
                violations.add(new Violation(Violation.Level.ERROR, call, Violation.Cause.MULTIPLE_DEFINITIONS));
            }
            else if(call.getAllPotentialKeywords(Link.Import.BOTH).isEmpty()){
                violations.add(new Violation(Violation.Level.ERROR, call, Violation.Cause.NO_DEFINITION_FOUND));
            }
        });
    }
}
