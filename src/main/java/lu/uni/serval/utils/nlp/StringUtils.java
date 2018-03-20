package lu.uni.serval.utils.nlp;

import lu.uni.serval.utils.UnorderedPair;
import opennlp.tools.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {
    private static CompareMemory<Integer> levenshteinDistanceMemory = new CompareMemory<Integer>();
    private static CompareMemory<Double> levenshteinIndexMemory = new CompareMemory<Double>();

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

    private static class CompareMemory<T>{
        private Map<UnorderedPair<String>, T> map;

        CompareMemory(){
            this.map = new HashMap<UnorderedPair<String>, T>();
        }

        public boolean isCached(String word1, String word2){
            return map.containsKey(new UnorderedPair<String>(word1, word2));
        }

        public T getScore(String word1, String word2){
            return map.get(new UnorderedPair<String>(word1, word2));
        }

        public void set(String string1, String string2, T score){
            this.map.put(new UnorderedPair<String>(string1, string2), score);
        }
    }
}
