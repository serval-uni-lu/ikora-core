package org.ikora.error;

public class ErrorMessages {
    private ErrorMessages() {}

    public static final String EMPTY_TOKEN_NOT_EXPECTED = "Empty token not expected";
    public static final String EMPTY_TOKEN_SHOULD_BE_KEYWORD = "Empty token should be a keyword call";
    public static final String FAILED_TO_CREATE_DEPENDENCY = "Failed to create dependency";
    public static final String FAILED_TO_ADD_STEP_TO_TEST_CASE = "Failed to add step to test case";
    public static final String FAILED_TO_ADD_STEP_TO_KEYWORD = "Failed to add step to keyword";
    public static final String EXPECTING_KEYWORD_CALL = "Expecting a keyword call without assignment";
    public static final String FOR_LOOP_NOT_ALLOWED_IN_TEST_CASE = "For loop not allow in test case";
    public static final String SHOULD_HANDLE_STATIC_IMPORT = "Should handle Static import at this point";
    public static final String FOUND_MULTIPLE_MATCHES = "Found multiple matches to resolve symbol";
    public static final String FOUND_NO_MATCH = "Found no match to resolve symbol";
    public static final String FAILED_TO_LOAD_LIBRARY_KEYWORD = "Failed to load library keyword";
}
