package lu.uni.serval.ikora.core.utils;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import org.apache.commons.lang3.tuple.Pair;
import lu.uni.serval.ikora.core.model.NodeList;
import lu.uni.serval.ikora.core.model.SourceNode;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.min;

public class LevenshteinDistance {
    private LevenshteinDistance() {}

    public static int stringDistance(String string1, String string2){
        int[][] distanceMatrix = levenshteinDistance(string1, string2);
        return distanceMatrix[string1.length()][string2.length()];
    }

    public static double stringIndex(String string1, String string2) {
        int size = Math.max(string1.length(), string2.length());

        if(size == 0){
            return 0;
        }

        return (double)stringDistance(string1, string2) / (double)size;
    }

    public static double[][] distanceMatrix(List<? extends SourceNode> before, List<? extends SourceNode> after) {
        double[][] d = new double[before.size() + 1][after.size() + 1];

        for(int i = 0; i <= before.size(); ++i){
            for(int j = 0; j <= after.size(); ++j){
                if(i == 0){
                    d[i][j] = j;
                }
                else if (j == 0){
                    d[i][j] = i;
                }
                else {
                    double substitution = d[i - 1][j - 1] + (before.get(i - 1).differences(after.get(j - 1)).isEmpty() ? 0. : 1.);
                    double addition = d[i - 1][j] + 1;
                    double subtraction = d[i][j - 1] + 1;

                    d[i][j] = min(substitution, addition, subtraction);
                }
            }
        }

        return d;
    }

    // taken from OpenNLP StringUtils
    public static int[][] levenshteinDistance(String left, String right) {
        int wordLength = left.length();
        int lemmaLength = right.length();

        int[][] distance = new int[wordLength + 1][lemmaLength + 1];
        if (wordLength == 0) {
            return distance;
        } else if (lemmaLength == 0) {
            return distance;
        } else {
            int i;
            for(i = 0; i <= wordLength; distance[i][0] = i++) {
            }

            for(i = 0; i <= lemmaLength; distance[0][i] = i++) {
            }

            for(i = 1; i <= wordLength; ++i) {
                int s_i = left.charAt(i - 1);

                for(int j = 1; j <= lemmaLength; ++j) {
                    byte cost;
                    if (s_i == right.charAt(j - 1)) {
                        cost = 0;
                    } else {
                        cost = 1;
                    }

                    distance[i][j] = min(distance[i - 1][j] + 1, distance[i][j - 1] + 1, distance[i - 1][j - 1] + cost);
                }
            }

            return distance;
        }
    }

    public static List<Edit> getDifferences(NodeList<? extends SourceNode> before, NodeList<? extends SourceNode> after){
        List<Edit> edits = new ArrayList<>();

        double[][] distances = LevenshteinDistance.distanceMatrix(before, after);

        int xPosition = before.size();
        int yPosition = after.size();

        double value = distances[xPosition][yPosition];
        double initialValue = value;

        while(xPosition > 0 || yPosition > 0){
            double substitution = xPosition > 0 && yPosition > 0 ? distances[xPosition - 1][yPosition - 1] : initialValue;
            double addition = yPosition > 0 ? distances[xPosition][yPosition - 1] : initialValue;
            double subtraction = xPosition > 0 ? distances[xPosition - 1][yPosition] : initialValue;

            // first check if steps are equal
            if(xPosition > 0 && yPosition > 0){
                SourceNode beforeStep = before.get(xPosition - 1);
                SourceNode afterStep = after.get(yPosition - 1);

                if(beforeStep.differences(afterStep).isEmpty()){
                    value = substitution;
                    xPosition -= 1;
                    yPosition -= 1;

                    edits.addAll(beforeStep.differences(afterStep));

                    continue;
                }
            }

            // then check for the rest
            if(substitution <= subtraction && substitution <= addition){
                SourceNode beforeStep = before.get(xPosition - 1);
                SourceNode afterStep = after.get(yPosition - 1);

                List<Edit> differences = beforeStep.differences(afterStep);

                if(value > substitution){
                    edits.addAll(differences);
                }

                value = substitution;
                xPosition -= 1;
                yPosition -= 1;
            }
            else if (subtraction <= addition){
                SourceNode beforeStep = before.get(xPosition - 1);
                edits.add(Edit.removeElement(beforeStep.getClass(), beforeStep));

                value = subtraction;
                xPosition -= 1;
            }
            else{
                SourceNode afterStep = after.get(yPosition - 1);
                edits.add(Edit.addElement(afterStep.getClass(), afterStep));

                value = addition;
                yPosition -= 1;
            }
        }

        return edits;
    }

    public static <T extends SourceNode> List<Pair<T, T>> getMapping(List<T> before, List<T> after){
        final List<Pair<T, T>> map = new ArrayList<>();

        double[][] distances = LevenshteinDistance.distanceMatrix(before, after);

        int xPosition = before.size();
        int yPosition = after.size();

        double value = distances[xPosition][yPosition];
        double initialValue = value;

        while(xPosition > 0 || yPosition > 0){
            double substitution = xPosition > 0 && yPosition > 0 ? distances[xPosition - 1][yPosition - 1] : initialValue;
            double addition = yPosition > 0 ? distances[xPosition][yPosition - 1] : initialValue;
            double subtraction = xPosition > 0 ? distances[xPosition - 1][yPosition] : initialValue;

            // first check if steps are equal
            if(xPosition > 0 && yPosition > 0){
                T beforeStep = before.get(xPosition - 1);
                T afterStep = after.get(yPosition - 1);

                if(beforeStep.differences(afterStep).isEmpty()){
                    value = substitution;
                    xPosition -= 1;
                    yPosition -= 1;

                    map.add(Pair.of(beforeStep, afterStep));

                    continue;
                }
            }

            // then check for the rest
            if(substitution <= subtraction && substitution <= addition){
                T beforeStep = before.get(xPosition - 1);
                T afterStep = after.get(yPosition - 1);

                if(value > substitution){
                    map.add(Pair.of(beforeStep, afterStep));
                }

                value = substitution;
                xPosition -= 1;
                yPosition -= 1;
            }
            else if (subtraction <= addition){
                value = subtraction;
                xPosition -= 1;
            }
            else{
                value = addition;
                yPosition -= 1;
            }
        }

        return map;
    }
}
