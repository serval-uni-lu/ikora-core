/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.analytics;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.runner.exception.InvalidTypeException;

import java.util.*;

public class ProjectStatistics {
    enum Metric{
        SIZE, CONNECTIVITY, SEQUENCE, LEVEL
    }

    private final Project project;

    private final Set<UserKeyword> userKeywords;
    private final Set<TestCase> testCases;

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

    private <T extends SourceNode> Map<Integer, Integer> getDistribution(Class<T> type, Metric metric) throws InvalidTypeException {
        Map<Integer, Integer> distribution = new TreeMap<>();

        for(SourceNode sourceNode : getNodes(type)){
            int value;
            switch (metric){
                case SIZE: value = KeywordStatistics.getSize(sourceNode).getTotalSize(); break;
                case CONNECTIVITY: value = KeywordStatistics.getConnectivity(sourceNode); break;
                case SEQUENCE: value = KeywordStatistics.getSequenceSize(sourceNode); break;
                case LEVEL: value = KeywordStatistics.getLevel(sourceNode); break;
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
