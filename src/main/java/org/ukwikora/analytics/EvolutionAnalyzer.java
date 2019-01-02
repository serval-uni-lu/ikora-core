package org.ukwikora.analytics;

import org.ukwikora.model.*;

import java.util.*;

public class EvolutionAnalyzer {
    private List<Project> projects;

    public EvolutionAnalyzer(){
        projects = new ArrayList<>();
    }

    public static EvolutionAnalyzer fromGit(String gitUrl, String branch, String username, String password) {
        EvolutionAnalyzer analyzer = new EvolutionAnalyzer();

        GitRepository repository = new GitRepository(gitUrl, branch, username, password);
        List<GitCommit> commits = repository.getRevisions();

        for(GitCommit commit: commits){
            repository.checkout(commit.getId());
            Project project = repository.getProject();

            analyzer.projects.add(project);
        }

        return analyzer;
    }


    public EvolutionResults findDifferences(){
        EvolutionResults results = new EvolutionResults();

        Project project1 = null;
        for(Project project2: projects){
            if(project1 == null){
                project1 = project2;
                continue;
            }

            for(ElementInfoPair<UserKeyword> keywordPair: ElementMatcher.getPairs(UserKeyword.class, project1, project2)){
                UserKeyword keyword1 = keywordPair.getElement(project1);
                UserKeyword keyword2 = keywordPair.getElement(project2);

                results.addDifference(project1, Difference.of(keyword1, keyword2));
            }

            for(ElementInfoPair<TestCase> testCasePair: ElementMatcher.getPairs(TestCase.class, project1, project2)) {
                TestCase testCase1 = testCasePair.getElement(project1);
                TestCase testCase2 = testCasePair.getElement(project2);

                Sequence sequence1 = testCase1 != null ? testCase1.getMaxSequence() : null;
                Sequence sequence2 = testCase2 != null ? testCase2.getMaxSequence() : null;

                results.addDifference(project1, Difference.of(testCase1, testCase2), Difference.of(sequence1, sequence2));

                results.addSequence(project1, sequence1);
                results.addSequence(project2, sequence2);
            }

            for(ElementInfoPair<Variable> variablePair: ElementMatcher.getPairs(Variable.class, project1, project2)) {
                Variable variable1 = variablePair.getElement(project1);
                Variable variable2 = variablePair.getElement(project2);

                results.addDifference(project1, Difference.of(variable1, variable2));
            }

            results.addProject(project1);
            results.addProject(project2);

            project1 = project2;
        }

        return results;
    }
}
