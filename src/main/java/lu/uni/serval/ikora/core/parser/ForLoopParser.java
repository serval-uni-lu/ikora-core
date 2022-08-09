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
package lu.uni.serval.ikora.core.parser;

import lu.uni.serval.ikora.core.exception.MalformedVariableException;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ForLoopParser {
    private static final Pattern inPattern = Pattern.compile("^IN(\\s?)");

    private ForLoopParser() {}

    public static ForLoop parse(int indent, LineReader reader, Token forToken, Iterator<Token> tokenIterator, ErrorManager errors) throws IOException {
        if(forToken == null || forToken.isEmpty()){
            errors.registerInternalError(
                    reader.getSource(),
                    ErrorMessages.FAILED_TO_PARSE_FORLOOP,
                    Range.fromLine(reader.getCurrent())
            );
        }

        Variable iterator = extractIterator(reader, tokenIterator, errors);
        Interval interval = extractInterval(reader, tokenIterator, errors);

        List<Step> steps = new ArrayList<>();

        while (reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()) {
                reader.readLine();
                continue;
            }

            if(indent + 1 != LexerUtils.peek(reader.getCurrent()).getIndentSize()){
                break;
            }

            final Tokens contentTokens = LexerUtils.tokenize(reader);
            final Iterator<Token> contentTokenIterator = TokenScanner.from(contentTokens)
                    .skipTypes(Token.Type.CONTINUATION)
                    .skipIndent(true)
                    .iterator();

            final Step step = StepParser.parse(contentTokens.getIndentSize(), reader, contentTokenIterator.next(), contentTokenIterator, false, errors);
            steps.add(step);
        }

        return new ForLoop(forToken, interval.in, iterator, interval.range, steps);
    }

    private static Variable extractIterator(LineReader reader, Iterator<Token> tokenIterator, ErrorManager errors) {
        Variable variable = Variable.invalid();

        if(!tokenIterator.hasNext()){
            errors.registerInternalError(
                    reader.getSource(),
                    ErrorMessages.FAILED_TO_LOCATE_ITERATOR_IN_FOR_LOOP,
                    Range.fromLine(reader.getCurrent())
            );
        }
        else{
            final Token token = tokenIterator.next();

            try {
                variable = Variable.create(token);
            } catch (MalformedVariableException e) {
                errors.registerInternalError(
                        reader.getSource(),
                        ErrorMessages.FAILED_TO_CREATE_ITERATOR_IN_FOR_LOOP,
                        Range.fromToken(token)
                );
            }
        }

        return variable;
    }

    private static Interval extractInterval(LineReader reader, Iterator<Token> tokenIterator, ErrorManager errors) {
        if(!tokenIterator.hasNext()){
            errors.registerInternalError(
                    reader.getSource(),
                    ErrorMessages.FOR_LOOP_SHOULD_HAVE_RANGE,
                    Range.fromLine(reader.getCurrent())
            );

            return new Interval(Token.empty(), null);
        }

        Token current = tokenIterator.next();
        Token inToken;

        if(StringUtils.matchesIgnoreCase(current, "IN")){
            inToken = current;
            if(tokenIterator.hasNext()) current = tokenIterator.next();
        }
        else if(StringUtils.matchesIgnoreCase(current, "^IN(\\s?)(.+)")){
            inToken = current.extract(inPattern);
            current = current.trim(inPattern);
        }
        else {
            inToken = Token.empty();
            if(tokenIterator.hasNext()) current = tokenIterator.next();
        }

        SourceNode range;
        Optional<Variable> variable = VariableParser.parse(current);

        if(variable.isPresent()){
            range = variable.get();
        }
        else {
            range = KeywordCallParser.parse(reader, current, tokenIterator, false, errors);
        }

        return new Interval(inToken, range);
    }

    private static class Interval {
        Token in;
        SourceNode range;

        public Interval(Token in, SourceNode range) {
            this.in = in;
            this.range = range;
        }
    }
}
