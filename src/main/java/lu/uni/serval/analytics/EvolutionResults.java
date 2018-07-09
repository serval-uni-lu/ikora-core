package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lu.uni.serval.robotframework.model.Keyword;
import lu.uni.serval.robotframework.model.Project;
import lu.uni.serval.utils.UnorderedPair;

import java.util.*;

@JsonSerialize(using = EvolutionResultsSerializer.class)
public class EvolutionResults {
    private SortedSet<Project> projects;

    private Map<Project, Map<Project, Set<Difference>>> differences;
    private Map<Project, Map<Project, Set<UnorderedPair<Keyword>>>> keywords;

    EvolutionResults(){
        projects = new TreeSet<>();
        differences = new LinkedHashMap<>();
        keywords = new LinkedHashMap<>();
    }

    public void addDifference(Project project1, Project project2, Difference difference) {
        if(difference.isEmpty()){
            return;
        }

        projects.add(project1);

        update(project1, project2, difference, differences);
        update(project1, project2, UnorderedPair.of(difference.getLeft(), difference.getRight()), keywords);
    }

    public Set<Project> getComparedTo(Project project){
        return differences.getOrDefault(project,  new LinkedHashMap<>()).keySet();
    }

    public SortedSet<Project> getProjects(){
        return projects;
    }

    public Set<Difference> getDifferences(Project project1, Project project2){
        return differences.getOrDefault(project1,  new LinkedHashMap<>()).get(project2);
    }

    public boolean containsKeywords(Project project1, Project project2, Keyword keyword1, Keyword keyword2){
        Map<Project, Set<UnorderedPair<Keyword>>> comparedTo = keywords.get(project1);

        if(comparedTo == null){
            return false;
        }

        Set<UnorderedPair<Keyword>> list = comparedTo.get(project2);

        if(list == null){
            return false;
        }

        return list.contains(UnorderedPair.of(keyword1, keyword2));
    }

    private <T> void update(Project left, Project right, T value, Map<Project, Map<Project, Set<T>>> container){
        Map<Project, Set<T>> comparedTo = container.getOrDefault(left, new LinkedHashMap<>());
        Set<T> list = comparedTo.getOrDefault(right, new HashSet<>());

        list.add(value);

        comparedTo.put(right, list);
        container.put(left, comparedTo);
    }
}
