package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.Project;

import java.util.*;

public class EvolutionResults {
    private SortedSet<Project> projects;

    private Map<Project, Map<Project, Set<Difference>>> differences;
    private Map<Project, Map<Project, Set<Difference>>> sequenceDifferences;
    private List<TimeLine> timeLines;

    EvolutionResults(){
        projects = new TreeSet<>();
        differences = new LinkedHashMap<>();
        sequenceDifferences = new LinkedHashMap<>();
        timeLines = new ArrayList<>();
    }

    public void addDifference(Project project1, Project project2, Difference difference) {
        projects.add(project1);
        projects.add(project2);

        updateTimeLine(difference);

        if(difference.isEmpty()){
            return;
        }

        update(project1, project2, difference, differences);
    }

    public void addDifference(Project project1, Project project2, Difference difference, Difference sequenceDifference) {
        addDifference(project1, project2, difference);
        update(project1, project2, sequenceDifference, sequenceDifferences);
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

    public Set<Difference> getSequenceDifferences(Project project1, Project project2){
        return sequenceDifferences.getOrDefault(project1,  new LinkedHashMap<>()).getOrDefault(project2, new HashSet<>());
    }

    private void update(Project left, Project right, Difference difference, Map<Project, Map<Project, Set<Difference>>> container){
        Map<Project, Set<Difference>> comparedTo = container.getOrDefault(left, new LinkedHashMap<>());
        Set<Difference> list = comparedTo.getOrDefault(right, new HashSet<>());

        list.add(difference);

        comparedTo.put(right, list);
        container.put(left, comparedTo);
    }

    private void updateTimeLine(Difference difference){
        if(difference.getLeft() != null){
            for(TimeLine timeLine : timeLines){
                if(timeLine.add(difference)){
                    return;
                }
            }
        }

        TimeLine timeLine = new TimeLine();
        timeLine.add(difference);

        timeLines.add(timeLine);
    }

    public List<TimeLine> getTimeLines() {
        return timeLines;
    }
}
