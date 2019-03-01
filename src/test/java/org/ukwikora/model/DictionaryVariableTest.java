package org.ukwikora.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class DictionaryVariableTest {
    @Test
    public void checkNameMatching(){
        DictionaryVariable test1 = new DictionaryVariable("&{test1}");
        assertTrue(test1.matches("&{test1}"));
        assertTrue(test1.matches("&{TEST1}"));
        assertFalse(test1.matches("${test1}"));
        assertFalse(test1.matches("@{test1}"));
    }
}