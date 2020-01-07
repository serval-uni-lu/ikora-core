package org.ikora.error;

public class ErrorMessages {
    private ErrorMessages() {}

    static public String EMPTY_TOKEN_NOT_EXPECTED = "Empty token not expected";
    static public String EMPTY_TOKEN_SHOULD_BE_KEYWORD = "Empty token should be a keyword call";
    static public String FAILED_TO_CREATE_DEPENDENCY = "Failed to create dependency";
    static public String FAILED_TO_ADD_STEP_TO_TEST_CASE = "Failed to add step to test case";
    static public String FAILED_TO_ADD_STEP_TO_KEYWORD = "Failed to add step to keyword";
    static public String EXPECTING_KEYWORD_CALL = "Expecting a keyword call without assignment";
}
