package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.Project;
import lu.uni.serval.robotframework.model.Sequence;

import java.util.*;

public class EvolutionResults {
    private Set<Project> projects;

    private Map<Project, List<Sequence>> sequences;
    private Map<Project, List<Difference>> differences;
    private Map<Project, List<Difference>> sequenceDifferences;

    private List<TimeLine> timeLines;

    EvolutionResults(){
        projects = new HashSet<>();
        sequences = new LinkedHashMap<>();

        differences = new LinkedHashMap<>();
        sequenceDifferences = new LinkedHashMap<>();
        timeLines = new ArrayList<>();
    }

    public void addProject(Project project) {
        if(project == null){
            return;
        }

        projects.add(project);
    }

    public void addSequence(Project project, Sequence sequence){
        if(sequence == null){
            return;
        }

        List<Sequence> sequenceList = sequences.getOrDefault(project, new ArrayList<>());
        sequenceList.add(sequence);

        sequences.put(project, sequenceList);
    }

    public void addDifference(Project project, Difference difference) {
        if(difference == null){
            return;
        }

        updateTimeLine(difference);

        if(difference.isEmpty()){
            return;
        }

        update(project, difference, differences);
    }

    public void addDifference(Project project, Difference difference, Difference sequenceDifference) {
        addDifference(project, difference);

        if(sequenceDifference == null){
            return;
        }

        if(sequenceDifference.isEmpty()){
            return;
        }

        update(project, sequenceDifference, sequenceDifferences);
    }

    public List<Project> getProjects(){
        List<Project> projectList = new ArrayList<>(projects);

        projectList.sort(Comparator.comparing(Project::getEpoch));

        return projectList;
    }

    public List<Difference> getDifferences(Project project){
        return differences.getOrDefault(project,  new ArrayList<>());
    }

    public List<Difference> getSequenceDifferences(Project project){
        return sequenceDifferences.getOrDefault(project,  new ArrayList<>());
    }

    public List<Sequence> getSequence(Project project){
        return sequences.getOrDefault(project, new ArrayList<>());
    }

    private void update(Project project, Difference difference, Map<Project, List<Difference>> container){
        List<Difference> differences = container.getOrDefault(project, new ArrayList<>());
        differences.add(difference);
        container.put(project, differences);
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
