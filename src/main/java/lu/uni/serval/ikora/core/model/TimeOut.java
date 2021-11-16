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
import lu.uni.serval.ikora.core.builder.ValueResolver;
import lu.uni.serval.ikora.core.exception.MalformedVariableException;
import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.Runtime;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collections;
import java.util.List;

public class TimeOut extends SourceNode {
    private final Token name;
    private final Token errorMessage;

    private final Variable variable;
    private final TimeValue value;
    private final boolean isNone;

    public TimeOut(Token name, Token errorMessage) throws MalformedVariableException {
        addToken(name);
        addToken(errorMessage);

        this.name = name;
        this.errorMessage = errorMessage;

        if(ValueResolver.isVariable(this.name)){
            this.variable = Variable.create(this.name);
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

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public boolean matches(Token name) {
        return this.name.equalsIgnorePosition(name);
    }

    @Override
    public double distance(SourceNode other) {
        return 0;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        return Collections.emptyList();
    }

    @Override
    public Token getNameToken() {
        return name;
    }

    @Override
    public void execute(Runtime runtime) throws RunnerException {
        throw new NotImplementedException("Runner is not implemented yet");
    }
}
