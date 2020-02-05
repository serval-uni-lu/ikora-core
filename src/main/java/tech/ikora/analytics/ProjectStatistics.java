package tech.ikora.analytics;

import tech.ikora.exception.InvalidTypeException;
import tech.ikora.utils.StringUtils;
import tech.ikora.model.*;

import java.util.*;

public class ProjectStatistics {
    enum Metric{
        SIZE, CONNECTIVITY, SEQUENCE, METRIC, BRANCH_INDEX
    }

    private final Project project;

    private Set<UserKeyword> userKeywords;
    private Set<TestCase> testCases;

    public ProjectStatistics(Project project){
        this.project = project;
        userKeywords = this.project.getNodes(UserKeyword.class);
        testCases = this.project.getNodes(TestCase.class);
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

        Set<Node> keywords = new HashSet<>();
        keywords.addAll(userKeywords);
        keywords.addAll(testCases);

        for (Node keyword: keywords){
            length += StringUtils.countLines(((KeywordDefinition)keyword).getDocumentation());
        }

        return length;
    }

    public <T extends Node> Map<Integer, Integer> getSizeDistribution(Class<T> type) throws InvalidTypeException {
        return getDistribution(type, Metric.SIZE);
    }

    public <T extends Node> Map<Integer, Integer> getConnectivityDistribution(Class<T> type) throws InvalidTypeException {
        return getDistribution(type, Metric.CONNECTIVITY);
    }

    public <T extends Node> Map<Integer, Integer> getSequenceDistribution(Class<T> type) throws InvalidTypeException {
        return getDistribution(type, Metric.SEQUENCE);
    }

    public <T extends Node> Map<Integer, Integer> getLevelDistribution(Class<T> type) throws InvalidTypeException {
        return getDistribution(type, Metric.METRIC);
    }

    public <T extends Node> Map<String, Integer> getDeadCodeDistribution(Class<T> type) throws InvalidTypeException {
        Map<String, Integer> deadCode = new HashMap<>(2);

        for(Node node : getNodes(type)){
            deadCode.merge(node.isDeadCode() ? "Dead" : "Executed", node.getLoc(), Integer::sum);
        }

        return deadCode;
    }

    <T extends Node> Map<Integer, Integer> getBranchIndex(Class<T> type) throws InvalidTypeException {
        return getDistribution(type, Metric.BRANCH_INDEX);
    }

    private <T extends Node> Map<Integer, Integer> getDistribution(Class<T> type, Metric metric) throws InvalidTypeException {
        Map<Integer, Integer> distribution = new TreeMap<>();

        for(Node node : getNodes(type)){
            int value;
            switch (metric){
                case SIZE: value = KeywordStatistics.getSize(node); break;
                case CONNECTIVITY: value = KeywordStatistics.getConnectivity(node); break;
                case SEQUENCE: value = KeywordStatistics.getSequenceSize(node); break;
                case METRIC: value = KeywordStatistics.getLevel(node); break;
                //case BRANCH_INDEX: value = KeywordStatistics.getBranchIndex(); break;
                default: value = -1;
            }

            distribution.merge(value, 1, Integer::sum);
        }

        return distribution;
    }

    private <T extends Node> Set<T> getNodes(Class<T> type) throws InvalidTypeException {
        if(type == UserKeyword.class){
            return (Set<T>)userKeywords;
        }
        else if(type == TestCase.class){
            return (Set<T>)testCases;
        }

        throw new InvalidTypeException("Unhandled type " + type.getName());
    }
}
