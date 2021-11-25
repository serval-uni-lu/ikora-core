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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Library extends SourceNode {
    private final Token label;
    private final Token name;
    private final NodeList<Value> arguments;

    public Library(Token label, Token name, NodeList<Value> arguments) {
        this.label = label;
        this.name = name;
        this.arguments = arguments;
    }

    public Token getLabel() {
        return label;
    }

    @Override
    public String getName() {
        return this.name.getText();
    }

    public NodeList<Value> getArguments() {
        return arguments;
    }

    @Override
    public Token getDefinitionToken() {
        return this.name;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null) return Collections.singletonList(Edit.addElement(Library.class, this));
        if(other == this) return Collections.emptyList();
        if(this.getClass() != other.getClass()) return Collections.singletonList(Edit.changeType(other, this));

        final Library that = (Library) other;
        final List<Edit> differenceList = new ArrayList<>();

        if(!this.label.matches(that.label)){
            differenceList.add(Edit.changeLabel(this, that));
        }

        if(!this.name.matches(that.name)){
            differenceList.add(Edit.changeName(this, that));
        }

        this.arguments.differences(that.arguments);

        return differenceList;
    }

    @Override
    public boolean matches(Token name) {
        return this.name.matches(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public void execute(Runtime runtime) throws RunnerException {
        //runtime not implemented yet
    }
}
