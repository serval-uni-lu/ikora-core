package lu.uni.serval.utils.nlp;

import opennlp.tools.util.StringUtil;

import lu.uni.serval.utils.CompareCache;

public class StringUtils {
    private static CompareCache<String, Integer> levenshteinDistanceMemory = new CompareCache<>();
    private static CompareCache<String, Double> levenshteinIndexMemory = new CompareCache<>();

    public static int levenshteinDistance(String string1, String string2){
        if(levenshteinDistanceMemory.isCached(string1, string2)){
            return levenshteinDistanceMemory.getScore(string1, string2);
        }

        int[][] distanceMatrix = StringUtil.levenshteinDistance(string1, string2);
        int score = distanceMatrix[string1.length()][string2.length()];

        levenshteinDistanceMemory.set(string1, string2, score);

        return score;
    }

    public static double levenshteinIndex(String string1, String string2) {
        if(levenshteinIndexMemory.isCached(string1, string2)){
            return levenshteinIndexMemory.getScore(string1, string2);
        }

        int size = string1.length() > string2.length() ? string1.length() : string2.length();
        double score = (double)levenshteinDistance(string1, string2) / (double)size;

        levenshteinIndexMemory.set(string1, string2, score);

        return score;
    }
}
