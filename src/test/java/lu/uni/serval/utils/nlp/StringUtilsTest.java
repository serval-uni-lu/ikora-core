package lu.uni.serval.utils.nlp;

import lu.uni.serval.Globals;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void checkLevenshteinDistanceDifferentString() {
        String string1 = "First String";
        String string2 = "Second String";

        int distance = StringUtils.levenshteinDistance(string1, string2);

        assertEquals(6, distance);
    }

    @Test
    public void checkLevenshteinIndexDifferentString() {
        String string1 = "First String";
        String string2 = "Second String";

        double index = StringUtils.levenshteinIndex(string1, string2);

        assertEquals(0.461538461538, index, Globals.delta);
    }
}
