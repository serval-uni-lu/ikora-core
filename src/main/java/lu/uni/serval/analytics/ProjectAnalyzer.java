package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.*;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        return analyzer;
    }

    public void add(Project project){
        boolean inserted = false;

        for(int index = 0; index < projects.size(); ++index){
            if(projects.get(index).getDateTime().isAfter(project.getDateTime())){
                projects.add(index, project);
                inserted = true;
                break;
            }
        }

        if(!inserted){
            projects.add(project);
        }
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

                project1 = project2;
            }
        }

        return differences;
    }
}
