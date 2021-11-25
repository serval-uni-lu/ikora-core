package lu.uni.serval.ikora.core.builder;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.libraries.builtin.keywords.*;
import lu.uni.serval.ikora.core.libraries.builtin.variables.*;
import lu.uni.serval.ikora.core.model.*;

import java.io.*;
import java.util.*;

public class LibraryLoader {
    private LibraryLoader() {}

    public static LibraryResources load(ErrorManager errors) {
        final LibraryResources libraries = new LibraryResources();

        registerBuiltInKeywords(libraries);
        registerBuiltInVariable(libraries);

        loadExternalLibrariesInfo(libraries, errors);

        return libraries;
    }

    private static void registerBuiltInKeywords(LibraryResources libraries) {
        final Set<Class<? extends LibraryKeyword>> keywords = new HashSet<>();

        keywords.add(CallMethod.class);
        keywords.add(Catenate.class);
        keywords.add(Comment.class);
        keywords.add(ContinueForLoop.class);
        keywords.add(ContinueForLoopIf.class);
        keywords.add(ConvertToBinary.class);
        keywords.add(ConvertToBoolean.class);
        keywords.add(ConvertToBytes.class);
        keywords.add(ConvertToHex.class);
        keywords.add(ConvertToInteger.class);
        keywords.add(ConvertToNumber.class);
        keywords.add(ConvertToOctal.class);
        keywords.add(ConvertToString.class);
        keywords.add(CreateDictionary.class);
        keywords.add(CreateList.class);
        keywords.add(Evaluate.class);
        keywords.add(ExitForLoop.class);
        keywords.add(ExitForLoopIf.class);
        keywords.add(Fail.class);
        keywords.add(FatalError.class);
        keywords.add(GetCount.class);
        keywords.add(GetLength.class);
        keywords.add(GetLibraryInstance.class);
        keywords.add(GetTime.class);
        keywords.add(GetVariables.class);
        keywords.add(GetVariableValue.class);
        keywords.add(ImportLibrary.class);
        keywords.add(ImportResource.class);
        keywords.add(ImportVariables.class);
        keywords.add(KeywordShouldExist.class);
        keywords.add(LengthShouldBe.class);
        keywords.add(Log.class);
        keywords.add(LogMany.class);
        keywords.add(LogToConsole.class);
        keywords.add(LogVariables.class);
        keywords.add(NoOperation.class);
        keywords.add(PassExecutionIf.class);
        keywords.add(RegexpEscape.class);
        keywords.add(ReloadLibrary.class);
        keywords.add(RemoveTags.class);
        keywords.add(RepeatKeyword.class);
        keywords.add(ReplaceVariables.class);
        keywords.add(ReturnFromKeyword.class);
        keywords.add(ReturnFromKeywordIf.class);
        keywords.add(RunKeyword.class);
        keywords.add(RunKeywordAndContinueOnFailure.class);
        keywords.add(RunKeywordAndExpectError.class);
        keywords.add(RunKeywordAndIgnoreError.class);
        keywords.add(RunKeywordAndReturn.class);
        keywords.add(RunKeywordAndReturnIf.class);
        keywords.add(RunKeywordAndReturnStatus.class);
        keywords.add(RunKeywordIf.class);
        keywords.add(RunKeywordIfAllCriticalTestsPassed.class);
        keywords.add(RunKeywordIfAllTestsPassed.class);
        keywords.add(RunKeywordIfAnyCriticalTestsFailed.class);
        keywords.add(RunKeywordIfAnyTestFailed.class);
        keywords.add(RunKeywordIfTestFailed.class);
        keywords.add(RunKeywordIfTestPassed.class);
        keywords.add(RunKeywordIfTimeoutOccurred.class);
        keywords.add(RunKeywords.class);
        keywords.add(RunKeywordUnless.class);
        keywords.add(SetGlobalVariable.class);
        keywords.add(SetLibrarySearchOrder.class);
        keywords.add(SetLogLevel.class);
        keywords.add(SetSuiteDocumentation.class);
        keywords.add(SetSuiteMetadata.class);
        keywords.add(SetSuiteVariable.class);
        keywords.add(SetTags.class);
        keywords.add(SetTestDocumentation.class);
        keywords.add(SetTestMessage.class);
        keywords.add(SetTestVariable.class);
        keywords.add(SetVariable.class);
        keywords.add(SetVariableIf.class);
        keywords.add(ShouldBeEmpty.class);
        keywords.add(ShouldBeEqual.class);
        keywords.add(ShouldBeEqualAsIntegers.class);
        keywords.add(ShouldBeEqualAsNumbers.class);
        keywords.add(ShouldBeEqualAsStrings.class);
        keywords.add(ShouldBeTrue.class);
        keywords.add(ShouldContain.class);
        keywords.add(ShouldContainAny.class);
        keywords.add(ShouldContainXTimes.class);
        keywords.add(ShouldEndWith.class);
        keywords.add(ShouldMatch.class);
        keywords.add(ShouldMatchRegexp.class);
        keywords.add(ShouldNotBeTrue.class);
        keywords.add(ShouldNotBeEmpty.class);
        keywords.add(ShouldNotBeEqual.class);
        keywords.add(ShouldNotBeEqualAsIntegers.class);
        keywords.add(ShouldNotBeEqualAsNumbers.class);
        keywords.add(ShouldNotBeEqualAsStrings.class);
        keywords.add(ShouldNotContain.class);
        keywords.add(ShouldNotContainAny.class);
        keywords.add(ShouldNotEndWith.class);
        keywords.add(ShouldNotMatch.class);
        keywords.add(ShouldNotMatchRegexp.class);
        keywords.add(ShouldNotStartWith.class);
        keywords.add(ShouldStartWith.class);
        keywords.add(Sleep.class);
        keywords.add(VariableShouldExist.class);
        keywords.add(VariableShouldNotExist.class);
        keywords.add(WaitUntilKeywordSucceeds.class);

        keywords.forEach(libraries::registerKeyword);
    }

    private static void registerBuiltInVariable(LibraryResources libraries) {
        final Set<LibraryVariable> variables = new HashSet<>();

        variables.add(new Curdir());
        variables.add(new DebugFile());
        variables.add(new ElementSeparator());
        variables.add(new Empty());
        variables.add(new Execdir());
        variables.add(new False());
        variables.add(new KeywordMessage());
        variables.add(new KeywordStatus());
        variables.add(new LineSeparator());
        variables.add(new LogFile());
        variables.add(new LogLevel());
        variables.add(new Null());
        variables.add(new OutputDir());
        variables.add(new OutputFile());
        variables.add(new PathSeparator());
        variables.add(new PreviousTestMessage());
        variables.add(new PreviousTestName());
        variables.add(new PreviousTestStatus());
        variables.add(new ReportFile());
        variables.add(new Space());
        variables.add(new SuiteDocumentation());
        variables.add(new SuiteMessage());
        variables.add(new SuiteMetadata());
        variables.add(new SuiteName());
        variables.add(new SuiteSource());
        variables.add(new SuiteStatus());
        variables.add(new Tempdir());
        variables.add(new TestDocumentation());
        variables.add(new TestMessage());
        variables.add(new TestName());
        variables.add(new TestStatus());
        variables.add(new TestTags());
        variables.add(new True());

        variables.forEach(libraries::registerVariable);
    }

    private static void loadExternalLibrariesInfo(LibraryResources libraries, ErrorManager errors){
        try {
            final InputStream in = LibraryLoader.class.getResourceAsStream("/libraries.json");
            final ObjectMapper mapper = new ObjectMapper();
            final List<LibraryInfo> libraryInfos = mapper.readValue(in, new TypeReference<List<LibraryInfo>>(){});

            libraries.addExternalLibraries(libraryInfos);
        } catch (IOException e) {
            errors.registerIOError(
                    new Source(new File("libraries.json")),
                    "Failed to load internal file libraries.json containing definitions for library keywords"
            );
        }
    }
}
