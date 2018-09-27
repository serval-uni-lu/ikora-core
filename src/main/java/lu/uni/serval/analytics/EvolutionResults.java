package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.Project;
import lu.uni.serval.utils.Differentiable;
import lu.uni.serval.utils.UnorderedPair;

import java.util.*;

public class EvolutionResults {
    private SortedSet<Project> projects;

    private Map<Project, Map<Project, Set<Difference>>> differences;
    private List<Sequence> sequences;

    EvolutionResults(){
        projects = new TreeSet<>();
        differences = new LinkedHashMap<>();
        sequences = new ArrayList<>();
    }

    public void addDifference(Project project1, Project project2, Difference difference) {
        projects.add(project1);
        projects.add(project2);

        updateSequence(difference);

        if(difference.isEmpty()){
            return;
        }

        update(project1, project2, difference, differences);
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

    private <T> void update(Project left, Project right, T value, Map<Project, Map<Project, Set<T>>> container){
        Map<Project, Set<T>> comparedTo = container.getOrDefault(left, new LinkedHashMap<>());
        Set<T> list = comparedTo.getOrDefault(right, new HashSet<>());

        list.add(value);

        comparedTo.put(right, list);
        container.put(left, comparedTo);
    }

    private void updateSequence(Difference difference){
        if(difference.getLeft() != null){
            for(Sequence sequence: sequences){
                if(sequence.add(difference)){
                    return;
                }
            }
        }

        Sequence sequence = new Sequence();
        sequence.add(difference);

        sequences.add(sequence);
    }

    public List<Sequence> getSequences() {
        return sequences;
    }
}
