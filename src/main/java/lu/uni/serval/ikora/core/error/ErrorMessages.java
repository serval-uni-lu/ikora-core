/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.error;

public class ErrorMessages {
    private ErrorMessages() {}

    public static final String FOR_LOOP_SHOULD_HAVE_RANGE = "For loop should have a range defined";
    public static final String EMPTY_TOKEN_NOT_EXPECTED = "Empty token not expected";
    public static final String EMPTY_TOKEN_SHOULD_BE_KEYWORD = "Empty token should be a keyword call";
    public static final String EXPECTED_KEYWORD_CALL = "Expected a keyword call to follow";
    public static final String FAILED_TO_ADD_STEP_TO_TEST_CASE = "Failed to add step to test case";
    public static final String FAILED_TO_ADD_STEP_TO_KEYWORD = "Failed to add step to keyword";
    public static final String FOUND_NO_MATCH = "Found no match to resolve symbol";
    public static final String FAILED_TO_LOAD_LIBRARY_KEYWORD = "Failed to load library keyword";
    public static final String FAILED_TO_PARSE_FORLOOP = "Failed to parser for loop";
    public static final String FAILED_TO_LOCATE_ITERATOR_IN_FOR_LOOP = "Failed to locate iterator when parsing for loop";
    public static final String FAILED_TO_CREATE_ITERATOR_IN_FOR_LOOP = "Failed to create iterator when parsing for loop";
    public static final String TOO_MANY_METADATA_ARGUMENTS = "Too many metadata arguments";
    public static final String MISSING_METADATA_VALUE = "Missing metadata value";
    public static final String FAILED_TO_PARSE_PARAMETER = "Failed to parse parameter";
    public static final String FAILED_TO_PARSE_STEP = "Failed to parse step";
    public static final String ASSIGNMENT_SHOULD_HAVE_LEFT_HAND_OPERAND = "Assignment should have left hand operand";
    public static final String ASSIGNMENT_SHOULD_START_WITH_VARIABLE = "Assignment should start with a variable";
    public static final String TOO_MANY_KEYWORD_ARGUMENTS = "Too many keywords arguments";
}
