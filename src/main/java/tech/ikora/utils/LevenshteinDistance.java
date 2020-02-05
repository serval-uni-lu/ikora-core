package tech.ikora.utils;

import tech.ikora.analytics.Action;
import tech.ikora.model.Differentiable;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.min;

public class LevenshteinDistance {
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

    public static double[][] distanceMatrix(List<? extends Differentiable> before, List<? extends Differentiable> after) {
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
                    double substitution = d[i - 1][j - 1] + before.get(i - 1).distance(after.get(j - 1));
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

    public static double index(List<? extends Differentiable> before, List<? extends Differentiable> after){
        double size = Math.max(before.size(), after.size());
        double distance = distanceMatrix(before, after)[before.size()][ after.size()];

        return size > 0 ? distance / size : 0;
    }

    public static List<Action> getDifferences(List<? extends Differentiable> before, List<? extends Differentiable> after){
        List<Action> actions = new ArrayList<>();

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
                Differentiable beforeStep = before.get(xPosition - 1);
                Differentiable afterStep = after.get(yPosition - 1);

                if(beforeStep.distance(afterStep) == 0){
                    value = substitution;
                    xPosition -= 1;
                    yPosition -= 1;

                    actions.addAll(beforeStep.differences(afterStep));

                    continue;
                }
            }

            // then check for the rest
            if(substitution < subtraction && substitution < addition){
                Differentiable beforeStep = before.get(xPosition - 1);
                Differentiable afterStep = after.get(yPosition - 1);

                List<Action> differences = beforeStep.differences(afterStep);

                if(value > substitution){
                    actions.addAll(differences);
                }

                value = substitution;
                xPosition -= 1;
                yPosition -= 1;
            }
            else if (subtraction < addition){
                Differentiable beforeStep = before.get(xPosition - 1);
                actions.add(Action.removeElement(beforeStep.getClass(), beforeStep));

                value = subtraction;
                xPosition -= 1;
            }
            else{
                Differentiable afterStep = after.get(yPosition - 1);
                actions.add(Action.addElement(afterStep.getClass(), afterStep));

                value = addition;
                yPosition -= 1;
            }
        }

        return actions;
    }
}