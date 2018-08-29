package lu.uni.serval.utils;

import opennlp.tools.util.StringUtil;

import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.min;

public class LevenshteinDistance {
    private static CompareCache<String, Integer> levenshteinDistanceMemory = new CompareCache<>();
    private static CompareCache<String, Double> levenshteinIndexMemory = new CompareCache<>();

    public static int stringDistance(String string1, String string2){
        if(levenshteinDistanceMemory.isCached(string1, string2)){
            return levenshteinDistanceMemory.getScore(string1, string2);
        }

        int[][] distanceMatrix = StringUtil.levenshteinDistance(string1, string2);
        int score = distanceMatrix[string1.length()][string2.length()];

        levenshteinDistanceMemory.set(string1, string2, score);

        return score;
    }

    public static double stringIndex(String string1, String string2) {
        if(levenshteinIndexMemory.isCached(string1, string2)){
            return levenshteinIndexMemory.getScore(string1, string2);
        }

        int size = string1.length() > string2.length() ? string1.length() : string2.length();
        double score = (double)stringDistance(string1, string2) / (double)size;

        levenshteinIndexMemory.set(string1, string2, score);

        return score;
    }

    public static <T> double[][] distanceMatrix(List<? extends Differentiable<T>> before, List<? extends Differentiable<T>> after) {
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
                    d[i][j] = min(d[i - 1][j - 1] + before.get(i - 1).indexTo(after.get(j - 1)),
                            d[i - 1][j] + 1,
                            d[i][j - 1] + 1);
                }
            }
        }

        return d;
    }

    public static <T> double index(List<? extends Differentiable<T>> before, List<? extends Differentiable<T>> after){
        double size = (double)(before.size() > after.size() ? before.size() : after.size());
        double distance = distanceMatrix(before, after)[before.size()][ after.size()];

        return size > 0 ? distance / size : 0;
    }
}
