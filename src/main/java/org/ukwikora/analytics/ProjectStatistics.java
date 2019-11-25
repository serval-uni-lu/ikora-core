package org.ukwikora.analytics;

import com.beust.jcommander.ParameterException;
import org.ukwikora.model.*;
import org.ukwikora.utils.StringUtils;

import java.util.*;

public class ProjectStatistics {
    enum Metric{
        Size, Connectivity, Sequence, Level, BranchIndex
    }

    final private Project project;

    private Set<UserKeyword> userKeywords;
    private Set<TestCase> testCases;

    public ProjectStatistics(Project project){
        this.project = project;
        userKeywords = this.project.getStatements(UserKeyword.class);
        testCases = this.project.getStatements(TestCase.class);
    }

    public int getNumberFiles(){
        return this.project.getTestCaseFiles().size();
    }

    public int getLoc(){
        return this.project.getLoc();
    }

    public <T extends KeywordDefinition> int getNumberKeywords(Class<T> type){
        Set<T> keywords = getStatements(type);

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

    public <T extends Node> Map<Integer, Integer> getSizeDistribution(Class<T> type){
        return getDistribution(type, Metric.Size);
    }

    public <T extends Node> Map<Integer, Integer> getConnectivityDistribution(Class<T> type){
        return getDistribution(type, Metric.Connectivity);
    }

    public <T extends Node> Map<Integer, Integer> getSequenceDistribution(Class<T> type){
        return getDistribution(type, Metric.Sequence);
    }

    public <T extends Node> Map<Integer, Integer> getLevelDistribution(Class<T> type){
        return getDistribution(type, Metric.Level);
    }

    public <T extends Node> Map<String, Integer> getDeadCodeDistribution(Class<T> type){
        Map<String, Integer> deadCode = new HashMap<>(2);

        for(Node node : getStatements(type)){
            deadCode.merge(node.isDeadCode() ? "Dead" : "Executed", node.getLoc(), Integer::sum);
        }

        return deadCode;
    }

    <T extends Node> Map<Integer, Integer> getBranchIndex(Class<T> type){
        return getDistribution(type, Metric.BranchIndex);
    }

    private <T extends Node> Map<Integer, Integer> getDistribution(Class<T> type, Metric metric){
        Map<Integer, Integer> distribution = new TreeMap<>();

        for(Node node : getStatements(type)){
            int value = -1;

            switch (metric){
                case Size: value = KeywordStatistics.getSize(node); break;
                case Connectivity: value = KeywordStatistics.getConnectivity(node); break;
                case Sequence: value = KeywordStatistics.getSequenceSize(node); break;
                case Level: value = KeywordStatistics.getLevel(node); break;
                //case BranchIndex: value = keyword.getBranchIndex(); break;
            }

            distribution.merge(value, 1, Integer::sum);
        }

        return distribution;
    }

    private <T extends Node> Set<T> getStatements(Class<T> type){
        if(type == UserKeyword.class){
            return (Set<T>)userKeywords;
        }
        else if(type == TestCase.class){
            return (Set<T>)testCases;
        }

        throw new ParameterException("Unhandled type " + type.getName());
    }
}
