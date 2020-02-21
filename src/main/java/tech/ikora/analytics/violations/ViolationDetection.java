package tech.ikora.analytics.violations;

import tech.ikora.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public static List<Violation> detect(Set<Project> projects, Violation.Cause cause){
        List<Violation> violations = new ArrayList<>();

        for(Project project: projects){
            violations.addAll(detect(project, cause));
        }

        return violations;
    }

    private static void detect(Violation.Cause cause, List<Violation> violations, List<Step> steps) {
        for(Step step: steps){
            if(step instanceof ForLoop){
                detect(cause, violations, ((ForLoop)step).getSteps());
            }

            switch (cause){
                case MULTIPLE_DEFINITIONS:
                    duplicatedKeyword(step, violations);
                    break;
                case TRANSITIVE_DEPENDENCY:
                    transitiveDependency(step, violations);
                    break;
                case NO_DEFINITION_FOUND:
                case INFINITE_LOOP:
                case LITERAL_LOCATOR:
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

            if(!step.getSourceFile().isDirectDependency(keyword.getSourceFile())){
                violations.add(new Violation(Violation.Level.WARNING, step, Violation.Cause.TRANSITIVE_DEPENDENCY));
            }
        });
    }
}
