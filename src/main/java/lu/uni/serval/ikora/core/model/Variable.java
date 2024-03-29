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
import lu.uni.serval.ikora.core.analytics.resolver.ValueResolver;
import lu.uni.serval.ikora.core.parser.VariableParser;
import lu.uni.serval.ikora.core.exception.MalformedVariableException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Variable extends Value {
    protected Token name;
    protected Pattern pattern;
    protected Link<Variable, Dependable> link;

    protected Variable(Token name) {
        setName(name);

        this.name.setType(Token.Type.VARIABLE);
        this.link = new Link<>(this);
    }

    public static Variable invalid() {
        return new InvalidVariable();
    }

    public void linkToDefinition(Dependable definition, Link.Import link){
        this.link.addNode(definition, link);
    }

    public Set<Dependable> getDefinition(Link.Import linkType){
        if(getAstParent().getClass() == VariableAssignment.class){
            return Collections.singleton((VariableAssignment)getAstParent());
        }

        return this.link.getAllLinks(linkType);
    }

    @Override
    public Token getDefinitionToken() {
        return this.name;
    }

    @Override
    public boolean matches(Token name) {
        String generic = ValueResolver.getGenericVariableName(name.getText());

        Matcher matcher = pattern.matcher(generic);
        return matcher.matches();
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.removeElement(this.getClass(), this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        if(this.getClass() != other.getClass()){
            return Collections.singletonList(Edit.changeValueType(this, other));
        }

        if(!this.getDefinitionToken().matches((other).getDefinitionToken())){
            return Collections.singletonList(Edit.changeValueName(this, other));
        }

        return Collections.emptyList();
    }

    public static Variable create(Token token) throws MalformedVariableException {
        Optional<Variable> variable = VariableParser.parse(token);

        if(!variable.isPresent()){
            throw new MalformedVariableException(String.format("Failed to create variable from value '%s'", token));
        }

        return variable.get();
    }

    @Override
    public String toString() {
        return name.getText();
    }

    protected abstract void setName(Token name);
}
