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
import lu.uni.serval.ikora.core.builder.VariableParser;
import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.Runtime;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TimeOut extends SourceNode {
    private final Token label;
    private final Token name;
    private final Token errorMessage;

    private final Variable variable;
    private final TimeValue value;
    private final boolean isNone;

    public TimeOut(Token label, Token name, Token errorMessage) {
        addToken(name);
        addToken(errorMessage);

        this.label = label;
        this.name = name;
        this.errorMessage = errorMessage;

        final Optional<Variable> optionalVariable = VariableParser.parse(this.name);

        if(optionalVariable.isPresent()){
            this.variable = optionalVariable.get();
            this.addAstChild(this.variable);
            this.value = null;
            this.isNone = false;
        }
        else if(TimeValue.isValid(this.name)){
            this.variable = null;
            this.value = new TimeValue(this.name);
            this.isNone = false;
        }
        else if (this.name.getText().equalsIgnoreCase("NONE")){
            this.variable = null;
            this.value = null;
            this.isNone = true;
        }
        else{
            this.variable = null;
            this.value = null;
            this.isNone = false;
        }
    }

    private TimeOut(){
        this.label = Token.empty();
        this.name = Token.empty();
        this.errorMessage = Token.empty();
        this.variable = null;
        this.value = null;
        this.isNone = false;
    }

    public static TimeOut none() {
        return new TimeOut();
    }

    public boolean isValid(){
        return this.variable != null || this.value != null || this.isNone;
    }

    public Token getLabel() {
        return label;
    }

    @Override
    public String getName() {
        return this.name.getText();
    }

    @Override
    public Token getDefinitionToken() {
        return name;
    }

    public Token getErrorMessage() {
        return errorMessage;
    }

    public Variable getVariable() {
        return variable;
    }

    public TimeValue getValue() {
        return value;
    }

    public boolean isNone() {
        return isNone;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public boolean matches(Token name) {
        return this.name.matches(name);
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        return Collections.emptyList();
    }

    @Override
    public void execute(Runtime runtime) throws RunnerException {
        throw new NotImplementedException("Runner is not implemented yet");
    }
}
