package tech.ikora.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.ikora.Helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LevenshteinDistanceTest {

    @Test
    void testLevenshteinDistanceDifferentString() {
        String string1 = "First String";
        String string2 = "Second String";

        int distance = LevenshteinDistance.stringDistance(string1, string2);

        assertEquals(6, distance);
    }

    @Test
    void testLevenshteinIndexDifferentString() {
        String string1 = "First String";
        String string2 = "Second String";

        double index = LevenshteinDistance.stringIndex(string1, string2);

        Assertions.assertEquals(0.461538461538, index, Helpers.epsilon);
    }
}
