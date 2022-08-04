package lu.uni.serval.ikora.core.parser;

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

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.*;

import java.io.IOException;
import java.util.Iterator;

class SettingsTableParser {
    private SettingsTableParser(){ }

    public static Settings parse(LineReader reader, Tokens blockTokens, ErrorManager errors) throws IOException {
        Settings settings = new Settings();
        settings.setHeader(ParserUtils.parseHeaderName(reader, blockTokens, errors));

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            final Tokens settingTokens = LexerUtils.tokenize(reader);
            final Iterator<Token> tokenIterator = TokenScanner.from(settingTokens)
                    .skipIndent(true)
                    .iterator();

            parseContentLine(settings, reader, tokenIterator, errors);
        }

        return settings;
    }

    private static void parseContentLine(Settings settings, LineReader reader, Iterator<Token> tokenIterator, ErrorManager errors) {
        final Token label = ParserUtils.getLabel(reader, tokenIterator, errors);

        if(DocumentationParser.is(label, Scope.SUITE)){
            final Documentation documentation = DocumentationParser.parse(label, tokenIterator);
            settings.setDocumentation(documentation);
        }
        else if(ResourcesParser.is(label)){
            final Resources resources = ResourcesParser.parse(label, tokenIterator);
            settings.addResources(resources);
        }
        else if(LibraryParser.is(label)){
            final Library library = LibraryParser.parse(label, tokenIterator);
            settings.addLibrary(library);
        }
        else if(VariableFileParser.is(label)) {
            final VariableFile variableFile = VariableFileParser.parse(label, tokenIterator);
            settings.addVariableFile(variableFile);
        }
        else if(MetadataParser.is(label)) {
            final Metadata metadata = MetadataParser.parse(reader, label, tokenIterator, errors);
            settings.addMetadata(metadata);
        }
        else if(TestProcessingParser.is(label, Scope.SUITE, TestProcessing.Phase.SETUP)) {
            final TestProcessing suiteSetup = TestProcessingParser.parse(TestProcessing.Phase.SETUP, reader, label, tokenIterator, errors);
            settings.setSuiteSetup(suiteSetup);
        }
        else if(TestProcessingParser.is(label, Scope.TEST, TestProcessing.Phase.SETUP)){
            final TestProcessing testSetup = TestProcessingParser.parse(TestProcessing.Phase.SETUP, reader, label, tokenIterator, errors);
            settings.setTestSetup(testSetup);
        }
        else if(TestProcessingParser.is(label, Scope.SUITE, TestProcessing.Phase.TEARDOWN)) {
            final TestProcessing teardown = TestProcessingParser.parse(TestProcessing.Phase.TEARDOWN, reader, label, tokenIterator, errors);
            settings.setSuiteTeardown(teardown);
        }
        else if(TestProcessingParser.is(label, Scope.TEST, TestProcessing.Phase.TEARDOWN)) {
            final TestProcessing teardown = TestProcessingParser.parse(TestProcessing.Phase.TEARDOWN, reader, label, tokenIterator, errors);
            settings.setTestTeardown(teardown);
        }
        else if(TestProcessingParser.is(label, Scope.TEST, TestProcessing.Phase.TEMPLATE)){
            final TestProcessing testTemplate = TestProcessingParser.parse(TestProcessing.Phase.TEMPLATE, reader, label, tokenIterator, errors);
            settings.setTestTemplate(testTemplate);
        }
        else if(TagsParser.is(label, Scope.SUITE, TagsParser.Type.FORCE)) {
            final NodeList<Literal> forceTags = TagsParser.parse(label, tokenIterator);
            settings.setForceTags(forceTags);
        }
        else if(TagsParser.is(label, Scope.SUITE, TagsParser.Type.DEFAULT)){
            final NodeList<Literal> defaultTags = TagsParser.parse(label, tokenIterator);
            settings.setDefaultTags(defaultTags);
        }
        else if(TimeoutParser.is(label, Scope.TEST)){
            final TimeOut testTimeout = TimeoutParser.parse(label, tokenIterator);
            settings.setTimeOut(testTimeout);
        }
    }
}
