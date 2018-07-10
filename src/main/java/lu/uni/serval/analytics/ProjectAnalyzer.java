package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.*;

import java.time.LocalDateTime;
import java.util.*;

public class ProjectAnalyzer {
    private List<Project> projects;

    public ProjectAnalyzer(){
        projects = new ArrayList<>();
    }

    public static ProjectAnalyzer fromGit(String gitUrl) {
        ProjectAnalyzer analyzer = new ProjectAnalyzer();

        GitRepository repository = new GitRepository(gitUrl);
        Map<String, LocalDateTime> revisions = repository.getRevisions();

        for(String commitId: revisions.keySet()){
            repository.checkout(commitId);
            Project project = repository.getProject();

            analyzer.projects.add(project);
        }

        Collections.sort(analyzer.projects, Comparator.comparing(Project::getDateTime));

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
