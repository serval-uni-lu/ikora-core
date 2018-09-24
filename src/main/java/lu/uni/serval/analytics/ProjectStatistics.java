package lu.uni.serval.analytics;

import com.beust.jcommander.ParameterException;
import lu.uni.serval.robotframework.model.*;

public class ProjectStatistics {
    enum Metric{
        Size, Connectivity, Sequence, Depth, BranchIndex
    }

    final private Project project;

    private ElementTable<UserKeyword> userKeywords;
    private ElementTable<TestCase> testCases;

    ProjectStatistics(Project project){
        this.project = project;
        userKeywords = this.project.getElements(UserKeyword.class);
        testCases = this.project.getElements(TestCase.class);
    }

    int getNumberFiles(){
        return this.project.getTestCaseFiles().size();
    }

    int getLoc(){
        return this.project.getLoc();
    }

    <T extends KeywordDefinition> int getNumberKeywords(Class<T> type){
        ElementTable keywords = getKeywords(type);

        if(keywords != null){
            return keywords.size();
        }

        return 0;
    }

    <T extends KeywordDefinition> int[] getSizeDistribution(Class<T> type){
        return getDistribution(type, Metric.Size);
    }

    <T extends KeywordDefinition> int[] getConnectivityDistribution(Class<T> type){
        return getDistribution(type, Metric.Connectivity);
    }

    <T extends KeywordDefinition> int[] getSequenceDistribution(Class<T> type){
        return getDistribution(type, Metric.Sequence);
    }

    <T extends KeywordDefinition> int[] getDepthDistribution(Class<T> type){
        return getDistribution(type, Metric.Depth);
    }

    <T extends KeywordDefinition> int[] getBranchIndex(Class<T> type){
        return getDistribution(type, Metric.BranchIndex);
    }

    private <T extends KeywordDefinition> int[] getDistribution(Class<T> type, Metric metric){
        int[] distribution = new int[getNumberKeywords(type)];

        int index = 0;
        for(KeywordDefinition keyword: getKeywords(type)){
            int value = -1;

            switch (metric){
                case Size: value = keyword.getSize(); break;
                case Connectivity: value = keyword.getConnectivity(-1); break;
                case Sequence: value = keyword.getMaxSequenceSize(); break;
                case Depth: value = keyword.getDepth(); break;
                case BranchIndex: value = keyword.getBranchIndex(); break;
            }

            distribution[index++] = value;
        }

        return distribution;
    }

    private <T extends KeywordDefinition> ElementTable<T> getKeywords(Class<T> type){
        if(type == UserKeyword.class){
            return (ElementTable<T>) userKeywords;
        }
        else if(type == TestCase.class){
            return (ElementTable<T>) testCases;
        }

        throw new ParameterException("Unhandled type " + type.getName());
    }
}
