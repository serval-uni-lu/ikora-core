package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.GitRepository;
import lu.uni.serval.robotframework.model.Keyword;
import lu.uni.serval.robotframework.model.KeywordDefinition;
import lu.uni.serval.robotframework.model.Project;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

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

    public static ProjectAnalyzer parse(String gitUrl, String localFolder) {
        ProjectAnalyzer analyzer = new ProjectAnalyzer();

        GitRepository repository = new GitRepository(gitUrl, localFolder);
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


    public DifferenceResults findDifferences(){
        initKeywordSequence();

        DifferenceResults differences = new DifferenceResults();

        for(List<KeywordDefinition> sequence: sequences){
            KeywordDefinition previous = null;
            for(KeywordDefinition keyword: sequence){
                if(previous == null){
                    previous = keyword;
                    continue;
                }

                Pair keywordPair = ImmutablePair.of((Keyword)previous, (Keyword)keyword);

                if(differences.containsKey(keywordPair)){
                    previous = keyword;
                    continue;
                }

                differences.addDifference(Difference.of(previous, keyword));

                previous = keyword;
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
/*
            List<KeywordStatus> keywords = report.getKeywords();

            for(KeywordStatus keyword: keywords){
                if(!status.isServiceDown(keyword)){
                    sequences.add(keyword);
                }
            }
*/
        }
    }
}
