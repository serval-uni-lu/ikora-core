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
package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.parser.Line;

public class Range {
    private final Pointer start;
    private final Pointer end;

    public Range(Pointer start, Pointer end) {
        this.start = start;
        this.end = end;
    }

    public Pointer getStart() {
        return start;
    }

    public Pointer getEnd() {
        return end;
    }

    public boolean isValid(){
        return start != null && end != null;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", start.toString(), end.toString());
    }

    public static Range createInvalid(){
        return new Range(null, null);
    }

    public static Range fromTokens(Token startToken, Token endToken) {
        Pointer start = new Pointer(startToken.getLine(), startToken.getStartOffset());
        Pointer end = new Pointer(endToken.getLine(), endToken.getEndOffset());

        return new Range(start, end);
    }

    public static Range fromTokens(Tokens tokens) {
        return Range.fromTokens(tokens.first(), tokens.last());
    }

    public static Range fromToken(Token token){
        return Range.fromTokens(token, token);
    }

    public static Range fromLine(Line line) {
        if(line == null){
            return Range.createInvalid();
        }

        Pointer start = new Pointer(line.getNumber(), 0);
        Pointer end = new Pointer(line.getNumber(), line.getText().length());

        return new Range(start, end);
    }
}
