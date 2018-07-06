package lu.uni.serval.utils;

import lu.uni.serval.Globals;
import org.junit.Test;
import static org.junit.Assert.*;

public class LevenshteinDistanceTest {

    @Test
    public void checkLevenshteinDistanceDifferentString() {
        String string1 = "First String";
        String string2 = "Second String";

        int distance = LevenshteinDistance.stringDistance(string1, string2);

        assertEquals(6, distance);
    }

    @Test
    public void checkLevenshteinIndexDifferentString() {
        String string1 = "First String";
        String string2 = "Second String";

        double index = LevenshteinDistance.stringIndex(string1, string2);

        assertEquals(0.461538461538, index, Globals.delta);
    }
}
