package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.*;

import java.util.*;

public class ProjectAnalyzer {
    private List<Project> projects;

    public ProjectAnalyzer(){
        projects = new ArrayList<>();
    }

    public static ProjectAnalyzer fromGit(String gitUrl, String branch, String username, String password) {
        ProjectAnalyzer analyzer = new ProjectAnalyzer();

        GitRepository repository = new GitRepository(gitUrl, branch, username, password);
        List<GitCommit> commits = repository.getRevisions();

        commits = commits.subList(Math.max(0, commits.size() - 2), commits.size());

        for(GitCommit commit: commits){
            repository.checkout(commit.getId());
            Project project = repository.getProject();

            analyzer.projects.add(project);
        }

        return analyzer;
    }


    public EvolutionResults findDifferences(){
        EvolutionResults differences = new EvolutionResults();

        Project project1 = null;
        for(Project project2: projects){
            if(project1 == null){
                project1 = project2;
                continue;
            }

            for(KeywordInfoPair keywordPair: KeywordMatcher.getPairs(project1, project2)){
                KeywordDefinition keyword1 = keywordPair.getKeyword(project1);
                KeywordDefinition keyword2 = keywordPair.getKeyword(project2);

                if(differences.containsKeywords(project1, project2, keyword1, keyword2)){
                    project1 = project2;
                    continue;
                }

                differences.addDifference(project1, project2, Difference.of(keyword1, keyword2));
            }

            project1 = project2;
        }

        return differences;
    }
}
