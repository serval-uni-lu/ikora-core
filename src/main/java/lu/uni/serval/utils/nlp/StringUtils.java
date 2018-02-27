package lu.uni.serval.utils.nlp;

import opennlp.tools.util.StringUtil;

public class StringUtils {
    public static int levenshteinDistance(String string1, String string2){
        int[][] distanceMatrix = StringUtil.levenshteinDistance(string1, string2);
        return distanceMatrix[string1.length()][string2.length()];
    }

    public static double levenshteinIndex(String string1, String string2) {
        int size = string1.length() > string2.length() ? string1.length() : string2.length();
        return (double)levenshteinDistance(string1, string2) / (double)size;
    }
}
