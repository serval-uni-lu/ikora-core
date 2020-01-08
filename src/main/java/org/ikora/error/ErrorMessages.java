package org.ikora.error;

public class ErrorMessages {
    private ErrorMessages() {}

    public static final String EMPTY_TOKEN_NOT_EXPECTED = "Empty token not expected";
    public static final String EMPTY_TOKEN_SHOULD_BE_KEYWORD = "Empty token should be a keyword call";
    public static final String FAILED_TO_CREATE_DEPENDENCY = "Failed to create dependency";
    public static final String FAILED_TO_ADD_STEP_TO_TEST_CASE = "Failed to add step to test case";
    public static final String FAILED_TO_ADD_STEP_TO_KEYWORD = "Failed to add step to keyword";
    public static final String EXPECTING_KEYWORD_CALL = "Expecting a keyword call without assignment";
}
