package lu.uni.serval.ikora.core.model;

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

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.Runtime;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collections;
import java.util.List;

public class Literal extends Value {
    private final Token name;
    private final List<Variable> variables;

    public Literal(Token name) {
        this.name = name;
        this.variables = Collections.emptyList();

        addToken(this.name);
    }

    public Literal(Token name, List<Variable> variables) {
        this.name = name;
        this.variables = variables;

        addToken(this.name);
    }

    public List<Variable> getVariables() {
        return this.variables;
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
    public void execute(Runtime runtime) throws RunnerException {
        throw new NotImplementedException("Runner is not implemented yet");
    }

    @Override
    public Token getDefinitionToken() {
        return this.name;
    }

    @Override
    public double distance(SourceNode other) {
        if(this == other){
            return 0.;
        }

        if(other == null){
            return 1.;
        }

        if(Literal.class.isAssignableFrom(other.getClass())){
            return this.name.matches(((Literal)other).name) ? 0. : 1.;
        }

        return 1.;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.removeElement(this.getClass(), this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        if(!(other instanceof Literal)){
            return Collections.singletonList(Edit.changeValueType(this, other));
        }

        Literal literal = (Literal)other;
        if(!this.getName().equals(literal.getName())){
            return Collections.singletonList(Edit.changeValueName(this, other));
        }

        return Collections.emptyList();
    }
}
