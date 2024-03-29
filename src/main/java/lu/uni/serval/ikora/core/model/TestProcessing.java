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

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TestProcessing extends SourceNode {
    public enum Phase {
        SETUP,
        TEARDOWN,
        TEMPLATE
    }

    private final Phase phase;
    private final Token label;
    private final KeywordCall call;

    public TestProcessing(Phase phase, Token label, KeywordCall call) {
        this.phase = phase;
        this.label = label;

        this.call = call;
        addAstChild(this.call);
    }

    public Phase getPhase() {
        return phase;
    }

    public Token getLabel() {
        return label;
    }

    public Optional<KeywordCall> getCall() {
        return Optional.ofNullable(call);
    }

    @Override
    public boolean matches(Token name) {
        return false;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public Token getDefinitionToken() {
        if(this.call == null){
            return Token.empty();
        }

        return this.call.getDefinitionToken();
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null) return Collections.singletonList(Edit.removeElement(TestProcessing.class, this));
        if(other == this) return Collections.emptyList();
        if(this.getClass() != other.getClass()) return Collections.singletonList(Edit.changeType(this, other));

        final List<Edit> edits = new ArrayList<>();
        final TestProcessing that = (TestProcessing) other;

        if(this.getPhase() != that.getPhase()) edits.add(Edit.changeType(this, other));

        if(this.call == null && that.call != null){
            edits.add(Edit.addElement(KeywordCall.class, that.call));
        }
        else if(this.call != null){
            edits.addAll(this.call.differences(that.call));
        }

        return edits;
    }
}
