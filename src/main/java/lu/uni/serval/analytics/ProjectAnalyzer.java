package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectAnalyzer {
    private List<Project> projects;
    private KeywordSequence sequences;

    public ProjectAnalyzer(){
        projects = new ArrayList<>();
        sequences = null;
    }

    public static ProjectAnalyzer parse(String gitUrl) {
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

        sequences = null;
    }


    public EvolutionResults findDifferences(){
        initKeywordSequence();

        EvolutionResults differences = new EvolutionResults();

        for(List<KeywordInfo> sequence: sequences){
            KeywordInfo previous = null;
            for(KeywordInfo current: sequence){
                if(previous == null){
                    previous = current;
                    continue;
                }

                KeywordDefinition keyword1 = previous.getKeyword();
                KeywordDefinition keyword2 = current.getKeyword();

                Project project1 = previous.getProject();
                Project project2 = current.getProject();

                if(differences.containsKeywords(project1, project2, keyword1, keyword2)){
                    previous = current;
                    continue;
                }

                differences.addDifference(project1, project2, Difference.of(keyword1, keyword2));

                previous = current;
            }
        }

        return differences;
    }

    private void initKeywordSequence(){
        if(sequences != null){
            return;
        }

        sequences = new KeywordSequence();

        for(Project project: projects){
            for(KeywordDefinition keyword: project.getKeywords()){
                    sequences.add(project, keyword);
            }
        }
    }
}
