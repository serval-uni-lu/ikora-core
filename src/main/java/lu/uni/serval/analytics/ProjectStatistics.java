package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.*;

public class ProjectStatistics {
    enum Metric{
        Size, Complexity, Sequence, Depth
    }

    final private Project project;

    private ElementTable<UserKeyword> userKeywords;
    private ElementTable<TestCase> testCases;

    ProjectStatistics(Project project){
        this.project = project;
        userKeywords = null;
        testCases = null;
    }

    int getNumberFiles(){
        return this.project.getTestCaseFiles().size();
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

    <T extends KeywordDefinition> int[] getComplexityDistribution(Class<T> type){
        return getDistribution(type, Metric.Complexity);
    }

    <T extends KeywordDefinition> int[] getSequenceDistribution(Class<T> type){
        return getDistribution(type, Metric.Sequence);
    }

    <T extends KeywordDefinition> int[] getDepthDistribution(Class<T> type){
        return getDistribution(type, Metric.Depth);
    }


    private <T extends KeywordDefinition> int[] getDistribution(Class<T> type, Metric metric){
        int[] distribution = new int[getNumberKeywords(type)];

        int index = 0;
        for(KeywordDefinition keyword: getKeywords(type)){
            int value = -1;

            switch (metric){
                case Size: value = keyword.getSize(); break;
                case Complexity: value = keyword.getDependencies().size(); break;
                case Sequence: value = keyword.getMaxSequenceSize(); break;
                case Depth: value = keyword.getDepth(); break;
            }

            distribution[index++] = value;
        }

        return distribution;
    }

    private <T extends KeywordDefinition> ElementTable<T> getKeywords(Class<T> type){
        if(type == UserKeyword.class){
            if(userKeywords == null){
                userKeywords = (ElementTable<UserKeyword>)this.project.getElements(type);
            }

            return (ElementTable<T>) userKeywords;
        }
        else if(type == TestCase.class){
            if(testCases == null){
                testCases = (ElementTable<TestCase>)this.project.getElements(type);
            }

            return (ElementTable<T>) testCases;
        }

        return null;
    }
}
