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

import lu.uni.serval.ikora.core.exception.InvalidTypeException;
import lu.uni.serval.ikora.core.exception.MalformedVariableException;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.io.IOException;
import java.util.Optional;

class UserKeywordParser {
    private UserKeywordParser(){}

    public static UserKeyword parse(LineReader reader, Tokens nameTokens, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        Tokens tokens;

        final Optional<UserKeyword> optionalUserKeyword = ParserUtils.createKeyword(UserKeyword.class, reader, nameTokens.withoutIndent(), errors);

        if(!optionalUserKeyword.isPresent()){
            throw new IOException(String.format("failed to read keyword at line %d in file %s",
                    reader.getCurrent().getNumber(),
                    reader.getSource().getAbsolutePath()));
        }

        final UserKeyword userKeyword = optionalUserKeyword.get();

        while(reader.getCurrent().isValid() && LexerUtils.isSameBlock(nameTokens, reader)) {
            if(reader.getCurrent().ignore()) {
                reader.readLine();
                continue;
            }

            Tokens currentTokens = LexerUtils.tokenize(reader);

            tokens = currentTokens.withoutIndent();

            Token label = ParserUtils.getLabel(reader, tokens, errors);

            if (DocumentationParser.is(label)) {
                final Documentation documentation = DocumentationParser.parse(label, tokens);
                userKeyword.setDocumentation(documentation);
            }
            else if (StringUtils.compareNoCase(label, "\\[tags\\]")) {
                final NodeList<Literal> tags = ParserUtils.parseTags(label, tokens.withoutFirst());
                userKeyword.setTags(tags);
            }
            else if (StringUtils.compareNoCase(label, "\\[arguments\\]")) {
                NodeList<Variable> arguments = parseArguments(reader, label, tokens.withoutFirst(), errors);
                userKeyword.setParameters(arguments);
            }
            else if (StringUtils.compareNoCase(label, "\\[return\\]")) {
                NodeList<Value> returnValues = parseReturn(label, tokens.withoutFirst());
                userKeyword.setReturnVariables(returnValues);
            }
            else if (StringUtils.compareNoCase(label, "\\[teardown\\]")) {
                userKeyword.addToken(label.setType(Token.Type.LABEL));
                parseTeardown(reader, tokens.withoutFirst(), userKeyword, errors);
            }
            else if (StringUtils.compareNoCase(label, "\\[timeout\\]")) {
                userKeyword.addToken(label.setType(Token.Type.LABEL));
                ParserUtils.parseTimeOut(reader, tokens.withoutFirst(), userKeyword, errors);
            }
            else {
                parseStep(reader, currentTokens, userKeyword, dynamicImports, errors);
            }
        }

        return userKeyword;
    }

    private static NodeList<Variable> parseArguments(LineReader reader, Token label, Tokens values, ErrorManager errors) {
        NodeList<Variable> arguments = new NodeList<>(label);

        for(Token token: values){
            try {
                arguments.add(Variable.create(token));
            } catch (MalformedVariableException e) {
                errors.registerSyntaxError(
                        reader.getSource(),
                        String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_PARAMETER, e.getMessage()),
                        Range.fromToken(token, reader.getCurrent())
                );
            }
        }

        return arguments;
    }

    private static NodeList<Value> parseReturn(Token label, Tokens tokens) {
        NodeList<Value> returnValues = new NodeList<>(label.setType(Token.Type.LABEL));

        for(Token token: tokens){
            returnValues.add(ValueParser.parseValue(token));
        }

        return returnValues;
    }

    private static void parseTeardown(LineReader reader, Tokens tokens, UserKeyword userKeyword, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, false, errors);

        try {
            userKeyword.setTearDown(step);
            userKeyword.addTokens(step.getTokens());
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getSource(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TEARDOWN, e.getMessage()),
                    step.getRange()
            );
        }
    }

    private static void parseStep(LineReader reader, Tokens tokens, UserKeyword userKeyword, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, false, errors);

        try {
            userKeyword.addStep(step);
            dynamicImports.add(userKeyword, step);
            userKeyword.addTokens(step.getTokens());
        } catch (Exception e) {
           errors.registerSyntaxError(
                   step.getSourceFile().getSource(),
                   ErrorMessages.FAILED_TO_ADD_STEP_TO_KEYWORD,
                   step.getRange()
            );
        }
    }

}
