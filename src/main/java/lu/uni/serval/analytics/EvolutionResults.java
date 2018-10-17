package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.*;
import lu.uni.serval.utils.Differentiable;

import java.util.*;

public class EvolutionResults {
    public enum CoEvolutionType{
        CoEvolution, NoCoEvolution, NoChange, Invalid
    }

    private Set<Project> projects;

    private Map<Project, List<Sequence>> sequences;
    private Map<Project, List<Difference>> differences;
    private Map<Project, List<Difference>> sequenceDifferences;

    private List<TimeLine> timeLines;
    private DifferentiableMatcher timeLineMatcher;
    private List<TimeLine> timeLineNotChanged;
    private Map<Project, CloneResults> keywordClones;
    private Map<Project, CloneResults> testCaseClones;

    private Map<Differentiable, CoEvolutionType> coEvolutionTypes;

    EvolutionResults(){
        projects = new HashSet<>();
        sequences = new LinkedHashMap<>();

        differences = new LinkedHashMap<>();
        sequenceDifferences = new LinkedHashMap<>();
        timeLines = new ArrayList<>();

        timeLineMatcher = null;
        timeLineNotChanged = null;
        keywordClones = null;
        testCaseClones = null;

        coEvolutionTypes = null;
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

    public DifferentiableMatcher getTimeLinesMatches() {
        if(timeLineMatcher == null){
            List<TimeLine> timeLineChanged = new ArrayList<>(timeLines);
            timeLineChanged.removeAll(getNotChanged());

            timeLineMatcher = DifferentiableMatcher.match(timeLineChanged, 0.8);
        }

        return timeLineMatcher;
    }

    public List<TimeLine> getNotChanged(){
        if(timeLineNotChanged == null){
            timeLineNotChanged = new ArrayList<>();

            for(TimeLine timeLine: timeLines){
                if(!timeLine.hasChanged()){
                    timeLineNotChanged.add(timeLine);
                }
            }
        }

        return timeLineNotChanged;
    }

    public CoEvolutionType getCoEvolutionType(Differentiable differentiable){
        if(coEvolutionTypes == null){
            coEvolutionTypes = new HashMap<>();
            for(TimeLine notChanged: getNotChanged()){
                for(Differentiable notChangeItem: notChanged.getItems()){
                    coEvolutionTypes.put(notChangeItem, CoEvolutionType.NoChange);
                }
            }

            for(Map.Entry<Differentiable, Set<Differentiable>> timeLines: getTimeLinesMatches().getMatched().entrySet()){
                CoEvolutionType type = timeLines.getValue().size() > 0 ? CoEvolutionType.CoEvolution: CoEvolutionType.NoCoEvolution;

                for(Differentiable coEvolution: ((TimeLine)timeLines.getKey()).getItems()){
                    coEvolutionTypes.put(coEvolution, type);
                }
            }
        }

        return coEvolutionTypes.getOrDefault(differentiable, CoEvolutionType.Invalid);
    }

    public CloneResults getKeywordClones(Project project){
        if(keywordClones == null){
            keywordClones = new HashMap<>();

            for(Project current: projects){
                CloneResults<UserKeyword> cloneResults = CloneDetection.findClones(current, UserKeyword.class);
                keywordClones.put(current, cloneResults);
            }
        }

        return keywordClones.get(project);
    }

    public CloneResults getTestCaseClones(Project project){
        if(testCaseClones == null){
            testCaseClones = new HashMap<>();

            for(Project current: projects){
                CloneResults<TestCase> cloneResults = CloneDetection.findClones(current, TestCase.class);
                testCaseClones.put(current, cloneResults);
            }
        }

        return testCaseClones.get(project);
    }

    public int getTotalElement(Class<? extends Element> elementType, CloneResults.Type cloneType, CoEvolutionType coEvolutionType){
        int total = 0;

        for(Project project: projects){
            for (Element element: project.getElements(elementType)){
                if(!checkCloneCriterion(project, element, cloneType)){
                    continue;
                }

                if(!checkCoEvolutionCriterion(element, coEvolutionType)){
                    continue;
                }

                ++total;
            }
        }

        return total;
    }

    private boolean checkCloneCriterion(Project project, Element element, CloneResults.Type type){
        CloneResults clones = null;
        if(element.getClass() == UserKeyword.class){
            clones = getKeywordClones(project);
        }
        else if(element.getClass() == TestCase.class){
            clones = getTestCaseClones(project);
        }

        if(clones == null){
            return false;
        }

        return clones.getCloneType(element) == type;
    }

    private boolean checkCoEvolutionCriterion(Element element, CoEvolutionType type){
        CoEvolutionType found = getCoEvolutionType(element);
        return found == type;
    }
}
