package org.ikora.analytics;

import org.ikora.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ViolationDetection {
    public static List<Violation> detect(Project project){
        List<Violation> violations = new ArrayList<>();

        for(TestCase testCase: project.getTestCases()){
            for(Step step: testCase.getSteps()){
                duplicatedKeyword(step, violations);
                duplicatedVariable(step, violations);
                transitiveDependency(step, violations);
            }
        }

        for(UserKeyword userKeyword: project.getUserKeywords()){
            for(Step step: userKeyword.getSteps()){
                duplicatedKeyword(step, violations);
                duplicatedVariable(step, violations);
                transitiveDependency(step, violations);
            }
        }

        return violations;
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

    private static void duplicatedVariable(Step step, List<Violation> violations) {
    }

    private static void transitiveDependency(Step step, List<Violation> violations) {
        step.getKeywordCall().ifPresent(call -> {
            Set<Keyword> keywords = call.getAllPotentialKeywords(Link.Import.STATIC);

            if(keywords.size() != 1){
                return;
            }

            Keyword keyword = keywords.iterator().next();

            if(keyword == null){
                return;
            }

            if(keyword.getClass() != UserKeyword.class){
                return;
            }

            if(!step.getFile().isDirectDependency(keyword.getFile())){
                violations.add(new Violation(Violation.Level.WARNING, step, Violation.Cause.TRANSITIVE_DEPENDENCY));
            }
        });
    }



    public static List<Violation> detect(Set<Project> projects){
        List<Violation> violations = new ArrayList<>();

        for(Project project: projects){
            violations.addAll(detect(project));
        }

        return violations;
    }
}
