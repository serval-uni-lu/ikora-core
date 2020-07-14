package tech.ikora.analytics;

import tech.ikora.exception.InvalidTypeException;
import tech.ikora.model.*;

import java.util.*;

public class ProjectStatistics {
    enum Metric{
        SIZE, CONNECTIVITY, SEQUENCE, LEVEL, BRANCH_INDEX
    }

    private final Project project;

    private Set<UserKeyword> userKeywords;
    private Set<TestCase> testCases;

    public ProjectStatistics(Project project){
        this.project = project;
        userKeywords = this.project.getUserKeywords();
        testCases = this.project.getTestCases();
    }

    public int getNumberFiles(){
        return this.project.getSourceFiles().size();
    }

    public int getLoc(){
        return this.project.getLoc();
    }

    public <T extends KeywordDefinition> int getNumberKeywords(Class<T> type) throws InvalidTypeException {
        Set<T> keywords = getNodes(type);

        if(keywords != null){
            return keywords.size();
        }

        return 0;
    }

    public int getDocumentationLength() {
        int length = 0;

        Set<KeywordDefinition> keywords = new HashSet<>();
        keywords.addAll(userKeywords);
        keywords.addAll(testCases);

        for (KeywordDefinition keyword: keywords){
            length += keyword.getDocumentation().getLoc();
        }

        return length;
    }

    public <T extends SourceNode> Map<Integer, Integer> getSizeDistribution(Class<T> type) throws InvalidTypeException {
        return getDistribution(type, Metric.SIZE);
    }

    public <T extends SourceNode> Map<Integer, Integer> getConnectivityDistribution(Class<T> type) throws InvalidTypeException {
        return getDistribution(type, Metric.CONNECTIVITY);
    }

    public <T extends SourceNode> Map<Integer, Integer> getSequenceDistribution(Class<T> type) throws InvalidTypeException {
        return getDistribution(type, Metric.SEQUENCE);
    }

    public <T extends SourceNode> Map<Integer, Integer> getLevelDistribution(Class<T> type) throws InvalidTypeException {
        return getDistribution(type, Metric.LEVEL);
    }

    public <T extends SourceNode> Map<String, Integer> getDeadCodeDistribution(Class<T> type) throws InvalidTypeException {
        Map<String, Integer> deadCode = new HashMap<>(2);

        for(SourceNode sourceNode : getNodes(type)){
            deadCode.merge(sourceNode.isDeadCode() ? "Dead" : "Executed", sourceNode.getLoc(), Integer::sum);
        }

        return deadCode;
    }

    <T extends SourceNode> Map<Integer, Integer> getBranchIndex(Class<T> type) throws InvalidTypeException {
        return getDistribution(type, Metric.BRANCH_INDEX);
    }

    private <T extends SourceNode> Map<Integer, Integer> getDistribution(Class<T> type, Metric metric) throws InvalidTypeException {
        Map<Integer, Integer> distribution = new TreeMap<>();

        for(SourceNode sourceNode : getNodes(type)){
            int value;
            switch (metric){
                case SIZE: value = KeywordStatistics.getSize(sourceNode).getTotalSize(); break;
                case CONNECTIVITY: value = KeywordStatistics.getConnectivity(sourceNode); break;
                case SEQUENCE: value = KeywordStatistics.getSequenceSize(sourceNode); break;
                case LEVEL: value = KeywordStatistics.getLevel(sourceNode); break;
                //case BRANCH_INDEX: value = KeywordStatistics.getBranchIndex(); break;
                default: value = -1;
            }

            distribution.merge(value, 1, Integer::sum);
        }

        return distribution;
    }

    private <T extends SourceNode> Set<T> getNodes(Class<T> type) throws InvalidTypeException {
        if(type == UserKeyword.class){
            return (Set<T>)userKeywords;
        }
        else if(type == TestCase.class){
            return (Set<T>)testCases;
        }

        throw new InvalidTypeException("Unhandled type " + type.getName());
    }
}
