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

import java.util.ArrayList;
import java.util.List;

public class InvalidVariable extends Variable {
    public InvalidVariable() {
        super(Token.fromString("INVALID"));
    }

    @Override
    protected void setName(Token name) {
        this.name = name;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        //nothing to do
    }

    @Override
    public double distance(SourceNode other) {
        if(other instanceof InvalidVariable){
            return 0.0;
        }

        return 1.0;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        List<Edit> edits = new ArrayList<>();

        if(!(other instanceof InvalidStep)){
            edits.add(Edit.changeVariableDefinition(this, other));
        }

        return edits;
    }
}
