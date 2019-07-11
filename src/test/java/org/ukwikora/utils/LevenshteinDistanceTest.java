package org.ukwikora.utils;

import org.junit.jupiter.api.Test;
import org.ukwikora.Helpers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevenshteinDistanceTest {

    @Test
    void checkLevenshteinDistanceDifferentString() {
        String string1 = "First String";
        String string2 = "Second String";

        int distance = LevenshteinDistance.stringDistance(string1, string2);

        assertEquals(6, distance);
    }

    @Test
    void checkLevenshteinIndexDifferentString() {
        String string1 = "First String";
        String string2 = "Second String";

        double index = LevenshteinDistance.stringIndex(string1, string2);

        assertEquals(0.461538461538, index, Helpers.epsilon);
    }

    @Test
    void checkDistanceMatrix(){
        List<DifferentiableString> list1 = new ArrayList<>();
        list1.add(new DifferentiableString("Step 1"));
        list1.add(new DifferentiableString("Step 2"));
        list1.add(new DifferentiableString("Step 3"));

        List<DifferentiableString> list2 = new ArrayList<>();
        list2.add(new DifferentiableString("Step 2"));
        list2.add(new DifferentiableString("Step 3"));

        LevenshteinDistance.getDifferences(list1, list2);
    }
}
