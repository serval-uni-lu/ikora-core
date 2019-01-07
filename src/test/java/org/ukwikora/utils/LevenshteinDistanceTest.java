package org.ukwikora.utils;

import org.ukwikora.Globals;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void checkDistanceMatrix(){
        List<DifferentiableString> list1 = new ArrayList<>();
        list1.add(new DifferentiableString("Step 1"));
        list1.add(new DifferentiableString("Step 2"));
        list1.add(new DifferentiableString("Step 3"));

        List<DifferentiableString> list2 = new ArrayList<>();
        list2.add(new DifferentiableString("Step 2"));
        list2.add(new DifferentiableString("Step 3"));

        double[][] matrix = LevenshteinDistance.distanceMatrix(list1, list2);
        LevenshteinDistance.getDifferences(list1, list2);

        LevenshteinDistance.printMatrix(matrix);
    }
}
